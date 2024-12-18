# Use an official Maven image to perform the build
FROM maven:3.8.6-eclipse-temurin-17 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and source code into the container
COPY pom.xml /app/
COPY src /app/src

# Run the Maven build
RUN mvn clean package -DskipTests

# Use a base image with Java 17 JDK installed
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the build stage to the final stage
COPY --from=build /app/target/walkthru-0.0.1-SNAPSHOT.jar /app/walkthru-routing.jar

# Expose the port the app runs on
EXPOSE 8080

# Define the command to run the app
ENTRYPOINT ["java", "-jar", "/app/walkthru-routing.jar"]
