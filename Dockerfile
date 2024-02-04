# Start with a base image containing Java runtime
FROM openjdk:17-jdk-slim

# Add Labels
LABEL maintainer="caalzate91@github.com" \
      version="1.0" \
      description="Mi aplicación Spring Boot con Java 21 usando GCP Firestore" \
      vendor="Camilo Alzate"

# Make port 8080 available to the world outside this container
EXPOSE 8080

# The application's jar file
ARG JAR_FILE=build/libs/*.jar

# Add the application's jar to the container
ADD ${JAR_FILE} app.jar

# Copy the service account key file
COPY src/main/resources/credentials.json /var/credentials.json

# Set the GOOGLE_APPLICATION_CREDENTIALS environment variable
ENV GOOGLE_APPLICATION_CREDENTIALS=/var/credentials.json

# Run the jar file
ENTRYPOINT ["java","-jar","/app.jar"]