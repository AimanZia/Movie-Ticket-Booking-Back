# Use an official Maven image with OpenJDK 17 as the base image
FROM maven:3.8.3-openjdk-17-slim AS build

# Set the working directory in the container
WORKDIR /app

# Copy the Maven project and POM file into the container
COPY pom.xml .

# Download and install Maven dependencies
RUN mvn dependency:go-offline

# Copy the project source code into the container
COPY src ./src

# Build the application
RUN mvn package

# Use a lightweight base image for the final stage
FROM openjdk:17-jdk-slim

# Copy the JAR file from the previous stage
COPY --from=build /app/target/*.jar ./app.jar

# Specify the default command to run on container startup
CMD ["java", "-jar", "app.jar"]


