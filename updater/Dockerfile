FROM python:3.11-slim

# Instalamos dependencias del sistema necesarias para pip
RUN apt-get update && apt-get install -y --no-install-recommends \
    gcc \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copiamos requirements.txt e instalamos dependencias Python
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copiamos tu autoscaler
COPY autoscaler.py .

# Damos permiso de ejecución
RUN chmod +x autoscaler.py

# Variables por defecto (pueden ser reemplazadas en docker-compose)
ENV CHECK_INTERVAL=15 \
    COOLDOWN=120

CMD ["python", "/app/autoscaler.py"]
