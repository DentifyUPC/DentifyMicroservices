# Dockerfile (simple)
FROM eclipse-temurin:21-jre
ARG JAR_FILE=service-discovery-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /app.jar
EXPOSE 8761
ENTRYPOINT ["java","-jar","/app.jar"]