# Use an official OpenJDK runtime as the base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from your build directory into the container
COPY target/cheQR-0.0.1-SNAPSHOT.jar /app/app.jar 

# Expose the port your Spring Boot app runs on (default is 8080)
EXPOSE 8080

# Run the JAR file when the container starts
CMD ["java", "-jar", "app.jar"]