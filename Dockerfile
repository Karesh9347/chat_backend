# --- Stage 1: Build the JAR ---
# We use a Maven image with Java 21 to compile the code
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the application (skipping tests for faster deployment)
RUN mvn clean package -DskipTests

# --- Stage 2: Run the JAR ---
# We use a slim Java 21 image for the final container
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create the uploads directory you need
RUN mkdir -p uploads

# Copy the JAR from the 'build' stage to this final stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port
EXPOSE 8080

# Start the app
ENTRYPOINT ["java", "-jar", "app.jar"]
