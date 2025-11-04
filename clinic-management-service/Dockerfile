# Dockerfile (simple)
FROM eclipse-temurin:21-jre
ARG JAR_FILE=clinic-management-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /app.jar
EXPOSE 8085
ENTRYPOINT ["java","-jar","/app.jar"]