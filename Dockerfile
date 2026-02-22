# --- Stage 1: Build the application ---
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy only the files needed for the build to save time
COPY pom.xml .
COPY src ./src

# Run Maven to package the application
RUN mvn clean package -DskipTests

# --- Stage 2: Run the application ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create upload directory
RUN mkdir -p uploads

# Copy the JAR from the "build" stage
# Note: Use the wildcard *.jar so you don't have to worry about version names
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
