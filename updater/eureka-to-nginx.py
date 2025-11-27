#!/usr/bin/env python3
import time, requests, subprocess, docker, os

# ---------- CONFIG ----------
SERVICE = os.getenv("SERVICE")   # nombre usado en docker-compose (ajusta)
COMPOSE_PROJECT_DIR = ".."              #"/ruta/al/docker-compose" si usas docker compose desde otra ruta
MIN_REPLICAS = int(os.getenv("MIN_REPLICAS", "1"))
MAX_REPLICAS = int(os.getenv("MAX_REPLICAS", "4"))
CPU_UP_THRESHOLD = float(os.getenv("CPU_UP_THRESHOLD", "65"))   # % CPU promedio por instancia -> scale up
CPU_DOWN_THRESHOLD = float(os.getenv("CPU_DOWN_THRESHOLD", "30")) # % CPU promedio -> scale down
COOLDOWN = int(os.getenv("COOLDOWN", "120"))            # segundos entre acciones
DRAIN_TIMEOUT = int(os.getenv("DRAIN_TIMEOUT", "40"))        # segundos para drenar tras /admin/drain
CHECK_INTERVAL = int(os.getenv("CHECK_INTERVAL", "15"))
EUREKA_URL = "http://service-discovery:8761/eureka"
MANAGEMENT_PORT = 8079    # puerto donde está /admin/drain y actuator
# ----------------------------

client = docker.from_env()
last_action_ts = 0

def list_service_containers():
    """Devuelve los contenedores del servicio."""
    containers = []
    for c in client.containers.list():
        # Ajustable: nombre del contenedor contiene el nombre del servicio
        if SERVICE.replace("-", "") in c.name.replace("-", ""):
            containers.append(c)
    return containers


def current_scale():
    return len(list_service_containers())


def cpu_percent_for_container(container):
    """Cálculo básico de CPU basado en Docker stats."""
    try:
        stats = container.stats(stream=False)

        cpu_delta = (
            stats["cpu_stats"]["cpu_usage"]["total_usage"]
            - stats["precpu_stats"]["cpu_usage"]["total_usage"]
        )
        system_delta = (
            stats["cpu_stats"]["system_cpu_usage"]
            - stats["precpu_stats"]["system_cpu_usage"]
        )
        cores = len(stats["cpu_stats"]["cpu_usage"].get("percpu_usage", []))

        if system_delta > 0 and cpu_delta > 0:
            return (cpu_delta / system_delta) * cores * 100.0
        return 0.0

    except Exception as e:
        print("err cpu:", e)
        return 0.0


def avg_cpu():
    conts = list_service_containers()
    if not conts:
        return 0.0

    vals = [cpu_percent_for_container(c) for c in conts]
    vals = [v for v in vals if v is not None]

    if not vals:
        return 0.0

    return sum(vals) / len(vals)


def scale_up(target):
    print(f"[autoscaler] scaling UP → {target}")

    subprocess.check_call(
        f"cd {COMPOSE_PROJECT_DIR} && docker compose up -d --scale {SERVICE}={target}",
        shell=True
    )

    wait_for_new_instance_registration(timeout=120)


def wait_for_new_instance_registration(timeout=120):
    """Espera a que Eureka registre una nueva instancia."""
    t0 = time.time()
    while time.time() - t0 < timeout:
        try:
            r = requests.get(f"{EUREKA_URL}/apps/{SERVICE.upper()}")
            if r.status_code == 200 and "instance" in r.text:
                return True
        except:
            pass
        time.sleep(2)
    return False


def choose_down_candidate():
    """El contenedor más nuevo es el que se elimina primero."""
    conts = list_service_containers()
    if not conts:
        return None

    conts_sorted = sorted(conts, key=lambda c: c.attrs["Created"])
    return conts_sorted[-1]


def call_drain(container):
    """Llama al endpoint /admin/drain dentro de la instancia."""
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


def wait_instance_not_up_in_eureka(container, timeout=60):
    """Espera a que Eureka saque la instancia del registro."""
    cname = container.name
    t0 = time.time()

    while time.time() - t0 < timeout:
        try:
            r = requests.get(f"{EUREKA_URL}/apps/{SERVICE.upper()}", timeout=5)
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


def scale_down():
    candidate = choose_down_candidate()

    if not candidate:
        print("No candidate to downscale")
        return False

    print("Candidate to remove:", candidate.name)

    ok = call_drain(candidate)
    if not ok:
        print("Drain failed, continuing...")

    if not wait_instance_not_up_in_eureka(candidate, timeout=DRAIN_TIMEOUT):
        print("Timeout waiting Eureka, stopping anyway")

    stop_container(candidate)
    return True


def main_loop():
    global last_action_ts

    while True:
        try:
            if time.time() - last_action_ts < COOLDOWN:
                time.sleep(CHECK_INTERVAL)
                continue

            avg = avg_cpu()
            cur = current_scale()

            print(f"[autoscaler] avg_cpu={avg:.2f}% replicas={cur}")

            if avg > CPU_UP_THRESHOLD and cur < MAX_REPLICAS:
                target = min(MAX_REPLICAS, cur + 1)
                scale_up(target)
                last_action_ts = time.time()

            elif avg < CPU_DOWN_THRESHOLD and cur > MIN_REPLICAS:
                if scale_down():
                    last_action_ts = time.time()

            time.sleep(CHECK_INTERVAL)

        except Exception as e:
            print("Error in main loop:", e)
            time.sleep(10)


if __name__ == "__main__":
    main_loop()