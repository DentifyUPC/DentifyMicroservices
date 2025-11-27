FROM python:3.11-slim

# instalar nginx si quieres que updater haga nginx -t && reload (opcional)
# Si prefieres mantener updater ligero y no instalar nginx, la validación/reload se omitirá.
RUN apt-get update && apt-get install -y --no-install-recommends \
    nginx-core \
  && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY eureka-to-nginx.py .

# make script executable
RUN chmod +x eureka-to-nginx.py

# punto de montaje en runtime:
# - /etc/nginx-template/nginx.template  (plantilla)
# - /etc/nginx/nginx.conf              (salida)
VOLUME ["/etc/nginx", "/etc/nginx-template"]

ENV EUREKA_URL=http://eureka:8761/eureka/apps
ENV TEMPLATE_PATH=/etc/nginx-template/nginx.template
ENV OUTPUT_PATH=/etc/nginx/nginx.conf
ENV POLL_INTERVAL=15

CMD ["./eureka-to-nginx.py"]