# Use the official Gradle image to build the application
FROM gradle:latest AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle build files
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle/ ./gradle/

# Copy the source code
COPY src/ ./src/

# Build the application
RUN gradle build --no-daemon

# Use a lightweight base image for the final application image
FROM openjdk:11-jre-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the Gradle build stage to the final image
COPY --from=builder /app/build/libs/*.jar ./app.jar

# Command to run the application
CMD ["java", "-jar", "app.jar"]
