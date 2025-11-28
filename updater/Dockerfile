FROM python:3.11-slim

WORKDIR /app

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY eureka-to-nginx.py .

# No instalamos nginx
# No montamos /etc/nginx-template
# No montamos /etc/nginx

ENV EUREKA_URL=http://eureka:8761/eureka/apps
ENV POLL_INTERVAL=15

CMD ["python", "/app/eureka-to-nginx.py"]
