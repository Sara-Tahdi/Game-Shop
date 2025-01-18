# Stage 1: Build the application using Gradle with JDK 21
FROM gradle:8.12.0-jdk21 AS build

# Set the working directory
WORKDIR /app

# Copy the Gradle wrapper and other necessary files
COPY gradle/wrapper gradle/wrapper
COPY gradle-wrapper.properties gradle-wrapper.properties
COPY build.gradle build.gradle
COPY settings.gradle settings.gradle
COPY src src

# Run the Gradle build to create the JAR file
RUN gradle build --no-daemon

# Stage 2: Create the runtime image with JDK 21
FROM openjdk:21-jdk-slim

# Set the working directory for the runtime container
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the application port (default Spring Boot port)
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
