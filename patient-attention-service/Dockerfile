# Dockerfile (simple)
FROM eclipse-temurin:21-jre
ARG JAR_FILE=patient-attention-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /app.jar
EXPOSE 8082
EXPOSE 8079
ENTRYPOINT ["java","-jar","/app.jar"]