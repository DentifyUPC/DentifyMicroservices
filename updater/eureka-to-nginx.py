#!/usr/bin/env python3
"""
Autoscaler + (opcional) NGINX-updater que soporta múltiples servicios.

- SERVICES: lista coma-separada (ej: "api-gateway,patient,clinic")
- SCALE_SERVICE (o SERVICE): servicio que será observado/escalado
- Si NGINX_TEMPLATE_PATH está presente, genera nginx.conf y recarga nginx (si está disponible)
"""
import os
import time
import logging
import subprocess
import requests
import docker
import xml.etree.ElementTree as ET
from typing import List, Tuple

logging.basicConfig(level=logging.INFO, format="%(asctime)s %(levelname)s %(message)s")
log = logging.getLogger("autoscaler")

# ---------- CONFIG (read from env) ----------
SERVICES_ENV = os.getenv("SERVICES", "api-gateway")
SERVICES = [s.strip() for s in SERVICES_ENV.split(",") if s.strip()]

# Which service this instance will autoscalar (if you run multiple autoscalers)
SERVICE = os.getenv("SCALE_SERVICE", os.getenv("SERVICE", SERVICES[0] if SERVICES else None))
if not SERVICE:
    raise SystemExit("No SERVICE / SCALE_SERVICE provided and SERVICES is empty")

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

NGINX_TEMPLATE_PATH = os.getenv("NGINX_TEMPLATE_PATH", None)   # e.g. /etc/nginx/template/nginx.template
NGINX_OUTPUT_PATH = os.getenv("NGINX_OUTPUT_PATH", "/etc/nginx/nginx.conf")

# -------------------------------------------------------------------
client = docker.from_env()
last_action_ts = 0


# ---------- Eureka helpers ----------
def fetch_eureka_instances(app_name: str) -> List[Tuple[str, str, str]]:
    """
    Return list of (ip, port, instanceId) for app_name from Eureka.
    Handle XML responses used by default Eureka.
    """
    url = f"{EUREKA_URL.rstrip('/')}/apps/{app_name.upper()}"
    headers = {"Accept": "application/xml, application/json"}
    try:
        r = requests.get(url, headers=headers, timeout=5)
        if r.status_code != 200:
            log.debug("Eureka returned %s for %s", r.status_code, app_name)
            return []
        text = r.text.strip()
        # Eureka commonly returns XML. Parse XML robustly.
        try:
            root = ET.fromstring(text)
            instances = []
            for inst in root.findall(".//instance"):
                ip = (inst.findtext("ipAddr") or "").strip()
                port = ""
                port_elem = inst.find("port")
                if port_elem is not None:
                    # port element may be <port>8080</port> or <port enabled="true">8080</port>
                    port = (port_elem.text or port_elem.get("value") or "").strip()
                # fallback to homePageUrl parse
                if (not ip or not port) and inst.findtext("homePageUrl"):
                    try:
                        from urllib.parse import urlparse
                        p = urlparse(inst.findtext("homePageUrl"))
                        ip = ip or p.hostname or ""
                        port = port or (str(p.port) if p.port else "")
                    except Exception:
                        pass
                iid = inst.findtext("instanceId") or inst.findtext("hostName") or ""
                if ip and port:
                    instances.append((ip, port, iid))
            return instances
        except ET.ParseError:
            log.debug("Eureka response not XML; trying JSON parse")
            # try JSON minimal parse (if configured)
            try:
                j = r.json()
                instances = []
                apps = j.get("applications", {}).get("application", [])
                if isinstance(apps, dict):
                    apps = [apps]
                for app in apps:
                    for inst in app.get("instance", []) if isinstance(app.get("instance", []), list) else [app.get("instance", {})]:
                        ip = inst.get("ipAddr") or inst.get("hostName") or ""
                        port = ""
                        port_obj = inst.get("port") or {}
                        if isinstance(port_obj, dict):
                            port = port_obj.get("$") or port_obj.get("value") or ""
                        iid = inst.get("instanceId") or ""
                        if ip and port:
                            instances.append((ip, str(port), iid))
                return instances
            except Exception as ex:
                log.warning("Failed to parse Eureka response: %s", ex)
                return []
    except Exception as ex:
        log.warning("fetch_eureka_instances error for %s: %s", app_name, ex)
        return []


# ---------- NGINX template helpers ----------
def norm_upstream_name(service_name: str) -> str:
    return service_name.replace("-", "_") + "_up"


def placeholder_for(service_name: str) -> str:
    key = service_name.replace("-", "_").upper()
    return f"{{{{{key}_UPSTREAM}}}}"


def build_upstream_block(service: str, instances: List[Tuple[str, str, str]]) -> Tuple[str, str]:
    name = norm_upstream_name(service)
    lines = [f"upstream {name} {{"]
    if not instances:
        # keep a harmless server to avoid nginx failing completely; admin can edit template
        lines.append("    # no instances found")
    else:
        for ip, port, iid in instances:
            lines.append(f"    server {ip}:{port}; # id={iid}")
    lines.append("}")
    return "\n".join(lines), name


def read_template(path: str) -> str:
    with open(path, "r", encoding="utf-8") as f:
        return f.read()


def write_nginx_conf(content: str, out_path: str) -> bool:
    try:
        tmp = out_path + ".tmp"
        with open(tmp, "w", encoding="utf-8") as f:
            f.write(content)
        os.replace(tmp, out_path)
        log.info("Wrote nginx config to %s", out_path)
        return True
    except Exception as e:
        log.error("write_nginx_conf err: %s", e)
        return False


def test_and_reload_nginx(conf_path: str) -> bool:
    """
    Validates nginx config (nginx -t) and reloads nginx if binary available.
    If nginx not found, returns True (we still wrote file; reload may be performed outside).
    """
    try:
        # validate
        cmd = ["nginx", "-t"]
        subprocess.check_call(cmd, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
        # reload
        subprocess.check_call(["nginx", "-s", "reload"])
        log.info("nginx validated and reloaded")
        return True
    except FileNotFoundError:
        log.warning("nginx binary not found in this container; skipping reload")
        return True
    except subprocess.CalledProcessError as e:
        log.error("nginx -t/reload failed: %s", e)
        return False
    except Exception as e:
        log.exception("Unexpected error reloading nginx: %s", e)
        return False


def update_nginx_for_services(services: List[str]) -> bool:
    """
    Generate upstreams for each service and apply template replacements.
    If NGINX_TEMPLATE_PATH is not set, this is a no-op and returns True.
    """
    if not NGINX_TEMPLATE_PATH:
        log.debug("NGINX_TEMPLATE_PATH not set; skipping nginx update")
        return True

    try:
        template = read_template(NGINX_TEMPLATE_PATH)
    except Exception as e:
        log.error("Cannot read template %s: %s", NGINX_TEMPLATE_PATH, e)
        return False

    upstream_blocks = []
    mapping = {}
    for svc in services:
        inst = fetch_eureka_instances(svc)
        block, upname = build_upstream_block(svc, inst)
        upstream_blocks.append(block)
        mapping[placeholder_for(svc)] = upname

    full_upstreams = "\n\n".join(upstream_blocks)
    conf = template.replace("{{UPSTREAMS}}", full_upstreams)

    # replace placeholders like {{API_GATEWAY_UPSTREAM}}
    for ph, upn in mapping.items():
        conf = conf.replace(ph, upn)

    # also handle lowercase placeholders like {{api_gateway_upstream}}
    for svc in services:
        alt = "{{" + svc.replace("-", "_") + "_upstream}}"
        conf = conf.replace(alt, mapping.get(placeholder_for(svc), norm_upstream_name(svc)))

    ok = write_nginx_conf(conf, NGINX_OUTPUT_PATH)
    if not ok:
        return False

    return test_and_reload_nginx(NGINX_OUTPUT_PATH)


# ---------- Docker / autoscaling helpers ----------
def list_service_containers(srv: str):
    """Return list of running containers that belong to a service name (best-effort match)."""
    containers = []
    for c in client.containers.list():
        try:
            if srv.replace("-", "") in c.name.replace("-", ""):
                containers.append(c)
        except Exception:
            continue
    return containers


def current_scale(srv: str) -> int:
    return len(list_service_containers(srv))


def cpu_percent_for_container(container) -> float:
    try:
        stats = container.stats(stream=False)
        cpu_delta = (stats["cpu_stats"]["cpu_usage"]["total_usage"] - stats["precpu_stats"]["cpu_usage"]["total_usage"])
        system_delta = (stats["cpu_stats"]["system_cpu_usage"] - stats["precpu_stats"]["system_cpu_usage"])
        cores = len(stats["cpu_stats"]["cpu_usage"].get("percpu_usage", [])) or 1
        if system_delta > 0 and cpu_delta > 0:
            return (cpu_delta / system_delta) * cores * 100.0
        return 0.0
    except Exception as e:
        log.debug("cpu_percent_for_container error: %s", e)
        return 0.0


def avg_cpu(srv: str) -> float:
    conts = list_service_containers(srv)
    if not conts:
        return 0.0
    vals = [cpu_percent_for_container(c) for c in conts]
    vals = [v for v in vals if isinstance(v, (int, float))]
    return sum(vals) / len(vals) if vals else 0.0


def wait_for_new_instance_registration(srv: str, timeout: int = 120) -> bool:
    t0 = time.time()
    while time.time() - t0 < timeout:
        try:
            r = requests.get(f"{EUREKA_URL.rstrip('/')}/apps/{srv.upper()}", timeout=5)
            if r.status_code == 200 and "instance" in r.text:
                return True
        except Exception:
            pass
        time.sleep(2)
    return False


def scale_up(target: int, srv: str):
    log.info("Scaling UP %s -> %d", srv, target)
    try:
        subprocess.check_call(f"cd {COMPOSE_PROJECT_DIR} && docker compose up -d --scale {srv}={target}", shell=True)
        # wait registration then (optionally) update nginx
        wait_for_new_instance_registration(srv, timeout=120)
        update_nginx_for_services(SERVICES)
    except Exception as e:
        log.error("scale_up failed: %s", e)


def choose_down_candidate(srv: str):
    conts = list_service_containers(srv)
    if not conts:
        return None
    conts_sorted = sorted(conts, key=lambda c: c.attrs.get("Created", ""))
    return conts_sorted[-1]


def call_drain(container) -> bool:
    try:
        networks = container.attrs.get("NetworkSettings", {}).get("Networks", {})
        if not networks:
            log.debug("No networks for container %s", container.name)
            return False
        ip = list(networks.values())[0].get("IPAddress")
        if not ip:
            log.debug("No IP for container %s", container.name)
            return False
        url = f"http://{ip}:{MANAGEMENT_PORT}/actuator/drain"  # using actuator drain path (POST ?enable)
        # If you use custom WriteOperation drain: POST /actuator/drain?enable=true
        try:
            r = requests.post(f"{url}?enable=true", timeout=5)
            return r.status_code in (200, 204)
        except Exception as e:
            log.warning("call_drain request failed: %s", e)
            return False
    except Exception as e:
        log.warning("call_drain error: %s", e)
        return False


def wait_instance_not_up_in_eureka(container, srv: str, timeout: int = 60) -> bool:
    cname = container.name
    t0 = time.time()
    while time.time() - t0 < timeout:
        try:
            r = requests.get(f"{EUREKA_URL.rstrip('/')}/apps/{srv.upper()}", timeout=5)
            if r.status_code == 200 and cname not in r.text:
                return True
        except Exception:
            pass
        time.sleep(2)
    return False


def stop_container(container):
    try:
        log.info("Stopping container %s", container.name)
        container.stop(timeout=30)
        container.remove()
        return True
    except Exception as e:
        log.error("Error stopping container %s: %s", getattr(container, "name", ""), e)
        return False


def scale_down(srv: str) -> bool:
    candidate = choose_down_candidate(srv)
    if not candidate:
        log.info("No candidate to downscale for %s", srv)
        return False
    log.info("Chosen candidate to remove: %s", candidate.name)
    drained = call_drain(candidate)
    if not drained:
        log.info("Drain call failed or not supported; will wait for Eureka or stop")
    if not wait_instance_not_up_in_eureka(candidate, srv, timeout=DRAIN_TIMEOUT):
        log.info("Timeout waiting for Eureka removal, stopping container anyway")
    ok = stop_container(candidate)
    if ok:
        update_nginx_for_services(SERVICES)
        return True
    return False


# ---------- main loop ----------
def main_loop():
    global last_action_ts
    # initial nginx update (safe to skip if no template)
    update_nginx_for_services(SERVICES)
    while True:
        try:
            if time.time() - last_action_ts < COOLDOWN:
                time.sleep(CHECK_INTERVAL)
                continue

            avg = avg_cpu(SERVICE)
            cur = current_scale(SERVICE)
            log.info("Service=%s avg_cpu=%.2f%% replicas=%d", SERVICE, avg, cur)

            if avg > CPU_UP_THRESHOLD and cur < MAX_REPLICAS:
                target = min(MAX_REPLICAS, cur + 1)
                scale_up(target, SERVICE)
                last_action_ts = time.time()
            elif avg < CPU_DOWN_THRESHOLD and cur > MIN_REPLICAS:
                if scale_down(SERVICE):
                    last_action_ts = time.time()

            time.sleep(CHECK_INTERVAL)
        except Exception as e:
            log.exception("Error main loop: %s", e)
            time.sleep(10)


if __name__ == "__main__":
    log.info("Starting autoscaler for SERVICE=%s (services=%s)", SERVICE, SERVICES)
    main_loop()
