FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Create the upload directory so the app doesn't crash
RUN mkdir -p uploads

COPY target/*.jar app.jar

# Standard Spring Boot port
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]