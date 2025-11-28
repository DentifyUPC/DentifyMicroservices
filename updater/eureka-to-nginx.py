#!/usr/bin/env python3
"""
Autoscaler + NGINX updater que soporta múltiples servicios.
- Leer SERVICES desde env: coma-separado (ej: "api-gateway,patient,clinic")
- Genera upstreams para cada servicio consultando Eureka
- Reemplaza {{UPSTREAMS}} y placeholders tipo {{API_GATEWAY_UPSTREAM}} en nginx.template
- Recarga nginx si es posible
"""

import time, requests, subprocess, docker, os, xml.etree.ElementTree as ET

# ---------- CONFIG (ajusta con env o valores por defecto) ----------
SERVICES_ENV = os.getenv("SERVICES", "api-gateway")   # coma-separados
SERVICES = [s.strip() for s in SERVICES_ENV.split(",") if s.strip()]
SERVICE = os.getenv("SCALE_SERVICE", SERVICES[0] if SERVICES else "api-gateway")  # servicio que autoscalea
COMPOSE_PROJECT_DIR = os.getenv("COMPOSE_PROJECT_DIR", "..")
MIN_REPLICAS = int(os.getenv("MIN_REPLICAS", "1"))
MAX_REPLICAS = int(os.getenv("MAX_REPLICAS", "4"))
CPU_UP_THRESHOLD = float(os.getenv("CPU_UP_THRESHOLD", "65"))
CPU_DOWN_THRESHOLD = float(os.getenv("CPU_DOWN_THRESHOLD", "30"))
COOLDOWN = int(os.getenv("COOLDOWN", "120"))
DRAIN_TIMEOUT = int(os.getenv("DRAIN_TIMEOUT", "40"))
CHECK_INTERVAL = int(os.getenv("CHECK_INTERVAL", "15"))
EUREKA_URL = os.getenv("EUREKA_URL", "http://service-discovery:8761/eureka")
MANAGEMENT_PORT = int(os.getenv("MANAGEMENT_PORT", "8079"))
NGINX_TEMPLATE_PATH = os.getenv("NGINX_TEMPLATE_PATH", "./nginx.template")
NGINX_OUTPUT_PATH = os.getenv("NGINX_OUTPUT_PATH", "/etc/nginx/nginx.conf")
# -------------------------------------------------------------------

client = docker.from_env()
last_action_ts = 0

# ---------- utilidades ----------
def norm_upstream_name(service_name):
    """api-gateway -> api_gateway_up"""
    return service_name.replace("-", "_") + "_up"

def placeholder_for(service_name):
    """Genera placeholder que buscamos en template: API_GATEWAY_UPSTREAM"""
    key = service_name.replace("-", "_").upper()
    return f"{{{{{key}_UPSTREAM}}}}"

def fetch_eureka_instances(app_name):
    """Consulta Eureka y devuelve lista de (ip, port, instanceId)"""
    try:
        url = f"{EUREKA_URL}/apps/{app_name.upper()}"
        r = requests.get(url, timeout=5)
        if r.status_code != 200:
            # no registrado o error
            return []
        root = ET.fromstring(r.text)
        instances = []
        for inst in root.findall(".//instance"):
            ip = inst.findtext("ipAddr") or ""
            # puerto: buscar <port> or atributo
            port_elem = inst.find("port")
            port = None
            if port_elem is not None:
                port = port_elem.text or port_elem.get("value") or port_elem.get("port")
            if not port:
                # fallback
                port = "8080"
            iid = inst.findtext("instanceId") or inst.findtext("hostName") or ""
            instances.append((ip, port, iid))
        return instances
    except Exception as e:
        print("fetch_eureka_instances err:", e)
        return []

def build_upstream_block(service, instances):
    name = norm_upstream_name(service)
    lines = [f"upstream {name} {{"]
    if not instances:
        lines.append("    # no instances found")
    else:
        for ip, port, iid in instances:
            if not ip or not port:
                continue
            lines.append(f"    server {ip}:{port};  # id={iid}")
    lines.append("}")
    return "\n".join(lines), name

def read_template(path):
    with open(path, "r", encoding="utf-8") as f:
        return f.read()

def write_nginx_conf(content, out_path):
    try:
        with open(out_path, "w", encoding="utf-8") as f:
            f.write(content)
        return True
    except Exception as e:
        print("write_nginx_conf err:", e)
        return False

def test_and_reload_nginx(conf_path):
    try:
        test_cmd = ["nginx", "-t", "-c", conf_path] if conf_path != "/etc/nginx/nginx.conf" else ["nginx", "-t"]
        print("Running:", " ".join(test_cmd))
        subprocess.check_call(test_cmd)
        try:
            subprocess.check_call(["nginx", "-s", "reload"])
            print("nginx reloaded")
        except Exception as e:
            print("nginx reload failed (maybe no permissions):", e)
        return True
    except subprocess.CalledProcessError as e:
        print("nginx -t failed:", e)
        return False
    except FileNotFoundError:
        print("nginx not found in PATH")
        return False

# ---------- actualización nginx multi-servicio ----------
def update_nginx_for_services(services):
    try:
        template = read_template(NGINX_TEMPLATE_PATH)
    except Exception as e:
        print("Cannot read template:", e)
        return False

    upstreams = []
    # mapping placeholders -> upstream_name
    mapping = {}
    for svc in services:
        inst = fetch_eureka_instances(svc)
        block, upname = build_upstream_block(svc, inst)
        upstreams.append(block)
        # placeholder como {{API_GATEWAY_UPSTREAM}}
        mapping[placeholder_for(svc)] = upname

    full_upstreams = "\n\n".join(upstreams)
    conf = template.replace("{{UPSTREAMS}}", full_upstreams)

    # Reemplazar placeholders encontrados en la plantilla por sus nombres de upstream
    for ph, upn in mapping.items():
        if ph in conf:
            conf = conf.replace(ph, upn)
    # Adicional: si plantilla tiene {{API_GATEWAY_UPSTREAM}} en minúsculas variantes, intentar también
    # (ej: {{api_gateway_upstream}}) - reemplazar cualquier variante simple:
    for svc in services:
        alt1 = "{{" + svc.replace("-", "_") + "_upstream}}"
        if alt1 in conf:
            conf = conf.replace(alt1, mapping.get(placeholder_for(svc), norm_upstream_name(svc)))

    ok = write_nginx_conf(conf, NGINX_OUTPUT_PATH)
    if not ok:
        print("Failed to write nginx config to", NGINX_OUTPUT_PATH)
        return False

    test_and_reload_nginx(NGINX_OUTPUT_PATH)
    return True

# ---------- autoscaler (similar al tuyo, scalea SERVICE) ----------
def list_service_containers(srv):
    containers = []
    for c in client.containers.list():
        if srv.replace("-", "") in c.name.replace("-", ""):
            containers.append(c)
    return containers

def current_scale(srv):
    return len(list_service_containers(srv))

def cpu_percent_for_container(container):
    try:
        stats = container.stats(stream=False)
        cpu_delta = (stats["cpu_stats"]["cpu_usage"]["total_usage"] - stats["precpu_stats"]["cpu_usage"]["total_usage"])
        system_delta = (stats["cpu_stats"]["system_cpu_usage"] - stats["precpu_stats"]["system_cpu_usage"])
        cores = len(stats["cpu_stats"]["cpu_usage"].get("percpu_usage", []))
        if system_delta > 0 and cpu_delta > 0:
            return (cpu_delta / system_delta) * cores * 100.0
        return 0.0
    except Exception as e:
        print("err cpu:", e)
        return 0.0

def avg_cpu(srv):
    conts = list_service_containers(srv)
    if not conts:
        return 0.0
    vals = [cpu_percent_for_container(c) for c in conts]
    vals = [v for v in vals if v is not None]
    if not vals:
        return 0.0
    return sum(vals) / len(vals)

def scale_up(target, srv):
    print(f"[autoscaler] scaling UP {srv} → {target}")
    subprocess.check_call(f"cd {COMPOSE_PROJECT_DIR} && docker compose up -d --scale {srv}={target}", shell=True)
    wait_for_new_instance_registration(srv, timeout=120)
    # actualizar nginx para todos los servicios
    update_nginx_for_services(SERVICES)

def wait_for_new_instance_registration(srv, timeout=120):
    t0 = time.time()
    while time.time() - t0 < timeout:
        try:
            r = requests.get(f"{EUREKA_URL}/apps/{srv.upper()}", timeout=5)
            if r.status_code == 200 and "instance" in r.text:
                return True
        except:
            pass
        time.sleep(2)
    return False

def choose_down_candidate(srv):
    conts = list_service_containers(srv)
    if not conts:
        return None
    conts_sorted = sorted(conts, key=lambda c: c.attrs["Created"])
    return conts_sorted[-1]

def call_drain(container):
    try:
        networks = container.attrs["NetworkSettings"]["Networks"]
        ip = list(networks.values())[0]["IPAddress"]
        url = f"http://{ip}:{MANAGEMENT_PORT}/admin/drain"
        print(f"Calling DRAIN on {container.name} → {url}")
        r = requests.post(url, timeout=5)
        return r.status_code == 200
    except Exception as e:
        print("Error calling drain:", e)
        return False

def wait_instance_not_up_in_eureka(container, srv, timeout=60):
    cname = container.name
    t0 = time.time()
    while time.time() - t0 < timeout:
        try:
            r = requests.get(f"{EUREKA_URL}/apps/{srv.upper()}", timeout=5)
            if r.status_code == 200 and cname not in r.text:
                return True
        except:
            pass
        time.sleep(2)
    return False

def stop_container(container):
    try:
        print("Stopping container:", container.name)
        container.stop(timeout=30)
        container.remove()
        return True
    except Exception as e:
        print("Error stopping container:", e)
        return False

def scale_down(srv):
    candidate = choose_down_candidate(srv)
    if not candidate:
        print("No candidate to downscale for", srv)
        return False
    print("Candidate to remove:", candidate.name)
    ok = call_drain(candidate)
    if not ok:
        print("Drain failed, continuing...")
    if not wait_instance_not_up_in_eureka(candidate, srv, timeout=DRAIN_TIMEOUT):
        print("Timeout waiting Eureka, stopping anyway")
    if stop_container(candidate):
        update_nginx_for_services(SERVICES)
        return True
    return False

def main_loop():
    global last_action_ts
    # Generar config nginx al inicio para todos los servicios
    update_nginx_for_services(SERVICES)

    while True:
        try:
            if time.time() - last_action_ts < COOLDOWN:
                time.sleep(CHECK_INTERVAL)
                continue

            avg = avg_cpu(SERVICE)
            cur = current_scale(SERVICE)
            print(f"[autoscaler] service={SERVICE} avg_cpu={avg:.2f}% replicas={cur}")

            if avg > CPU_UP_THRESHOLD and cur < MAX_REPLICAS:
                target = min(MAX_REPLICAS, cur + 1)
                scale_up(target, SERVICE)
                last_action_ts = time.time()
            elif avg < CPU_DOWN_THRESHOLD and cur > MIN_REPLICAS:
                if scale_down(SERVICE):
                    last_action_ts = time.time()

            time.sleep(CHECK_INTERVAL)
        except Exception as e:
            print("Error main loop:", e)
            time.sleep(10)

if __name__ == "__main__":
    main_loop()
