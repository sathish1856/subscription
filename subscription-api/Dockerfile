# Use an official Gradle image to build the project
FROM gradle:7.2.0-jdk17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle wrapper and source code
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# Run the Gradle build, skipping tests
RUN ./gradlew build --no-daemon -x test

# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built jar from the build image
COPY --from=build /app/build/libs/*.jar app.jar

# Expose port 8080 to the outside world
EXPOSE 8080

# Command to run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
