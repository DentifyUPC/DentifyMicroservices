FROM eclipse-temurin:21-jre
RUN apt-get update && \
    apt-get install -y curl && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*
WORKDIR /app
ARG JAR_FILE=patient-attention-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /app.jar
EXPOSE 8082 8079
ENTRYPOINT ["java","-jar","/app.jar"]