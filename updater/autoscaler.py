#!/usr/bin/env python3
import time
import requests
import subprocess
import docker
import os

# -----------------------------
# CONFIG DESDE ENVIRONMENT
# -----------------------------
SERVICE = os.getenv("SERVICE")                       # nombre en docker-compose
MIN_REPLICAS = int(os.getenv("MIN_REPLICAS", "1"))
MAX_REPLICAS = int(os.getenv("MAX_REPLICAS", "4"))
CPU_UP_THRESHOLD = float(os.getenv("CPU_UP_THRESHOLD", "70"))
CPU_DOWN_THRESHOLD = float(os.getenv("CPU_DOWN_THRESHOLD", "30"))
COOLDOWN = int(os.getenv("COOLDOWN", "120"))
CHECK_INTERVAL = int(os.getenv("CHECK_INTERVAL", "15"))
MANAGEMENT_PORT = int(os.getenv("MANAGEMENT_PORT", "8079"))
DRAIN_TIMEOUT = 40   # valor interno, ya no por environment
EUREKA_URL = "http://service-discovery:8761/eureka/apps"

client = docker.from_env()
last_action_ts = 0

# -----------------------------
# UTILIDADES
# -----------------------------
def list_service_containers():
    """Devuelve todas las réplicas activas del servicio."""
    return [
        c for c in client.containers.list()
        if SERVICE in " ".join(c.name.split())
    ]

def current_scale():
    return len(list_service_containers())

def cpu_percent_for_container(container):
    """Obtiene %CPU usando docker stats (instantáneo)."""
    try:
        stats = container.stats(stream=False)

        cpu_delta = (
            stats["cpu_stats"]["cpu_usage"]["total_usage"] -
            stats["precpu_stats"]["cpu_usage"]["total_usage"]
        )
        system_delta = (
            stats["cpu_stats"]["system_cpu_usage"] -
            stats["precpu_stats"]["system_cpu_usage"]
        )

        if system_delta > 0 and cpu_delta > 0:
            cores = len(stats["cpu_stats"]["cpu_usage"].get("percpu_usage", []))
            return (cpu_delta / system_delta) * cores * 100.0
    except:
        pass
    return 0.0

def avg_cpu():
    conts = list_service_containers()
    if not conts:
        return 0.0
    vals = [cpu_percent_for_container(c) for c in conts]
    return sum(vals) / len(vals)

# -----------------------------
# SCALE UP (Traefik lo detecta solo)
# -----------------------------
def scale_up(target):
    print(f"[autoscaler] Scaling UP to {target} replicas")

    cmd = f"docker compose up -d --scale {SERVICE}={target}"
    subprocess.call(cmd, shell=True)

    print("[autoscaler] Waiting 10s to allow new replicas to start...")
    time.sleep(10)

# -----------------------------
# SCALE DOWN (con drain)
# -----------------------------
def choose_down_candidate():
    containers = list_service_containers()
    if not containers:
        return None

    containers_sorted = sorted(containers, key=lambda c: c.attrs["Created"])
    return containers_sorted[-1]  # el más nuevo

def call_drain(container):
    try:
        networks = container.attrs["NetworkSettings"]["Networks"]
        ip = list(networks.values())[0]["IPAddress"]

        url = f"http://{ip}:{MANAGEMENT_PORT}/admin/drain"
        print(f"[autoscaler] Calling drain on {container.name} -> {url}")
        r = requests.post(url, timeout=4)
        return r.status_code == 200
    except:
        return False

def stop_container(container):
    """Detiene y elimina la réplica."""
    try:
        print(f"[autoscaler] Stopping container {container.name}")
        container.stop(timeout=20)
        container.remove()
        return True
    except Exception as e:
        print("Error stopping:", e)
        return False

def scale_down():
    candidate = choose_down_candidate()
    if not candidate:
        print("[autoscaler] No candidate to scale down.")
        return False

    print(f"[autoscaler] Scaling DOWN – candidate: {candidate.name}")

    drained = call_drain(candidate)
    if drained:
        print("[autoscaler] Drain OK → waiting before stop")
        time.sleep(DRAIN_TIMEOUT)
    else:
        print("[autoscaler] Drain FAILED → forcing stop")

    stop_container(candidate)
    return True

# -----------------------------
# MAIN LOOP
# -----------------------------
def main_loop():
    global last_action_ts

    while True:
        try:
            if time.time() - last_action_ts < COOLDOWN:
                time.sleep(CHECK_INTERVAL)
                continue

            avg = avg_cpu()
            replicas = current_scale()

            print(f"[autoscaler] CPU={avg:.2f}% replicas={replicas}")

            if avg > CPU_UP_THRESHOLD and replicas < MAX_REPLICAS:
                scale_up(replicas + 1)
                last_action_ts = time.time()

            elif avg < CPU_DOWN_THRESHOLD and replicas > MIN_REPLICAS:
                if scale_down():
                    last_action_ts = time.time()

            time.sleep(CHECK_INTERVAL)

        except Exception as e:
            print("Error in main loop:", e)
            time.sleep(10)

if __name__ == "__main__":
    main_loop()
