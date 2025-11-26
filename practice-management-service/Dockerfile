# Dockerfile (simple)
FROM eclipse-temurin:21-jre
ARG JAR_FILE=practice-management-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /app.jar
EXPOSE 8083
EXPOSE 8079
ENTRYPOINT ["java","-jar","/app.jar"]