# Base image
FROM maven:3.9.9-eclipse-temurin-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy all files from the build context to the working directory
COPY . .

# Debugging: List the contents of the /app directory
RUN ls -la /app

# Test Maven execution
RUN mvn -version