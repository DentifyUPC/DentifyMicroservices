# Dockerfile (simple)
FROM eclipse-temurin:21-jre
ARG JAR_FILE=api-gateway-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /app.jar
EXPOSE 8080
EXPOSE 8079
ENTRYPOINT ["java","-jar","/app.jar"]