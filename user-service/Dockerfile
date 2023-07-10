FROM eclipse-temurin:17-jdk-focal

LABEL version="0.0.2"
ARG IMAGE_TAG="0.0.2"
LABEL tag="${IMAGE_TAG}"
 
WORKDIR /app

# Copy the project's source code into the image
COPY . .

# Define the command to run the application
RUN ./mvnw dependency:go-offline
RUN ./mvnw clean install

# Build the application using Maven

CMD ["./mvnw", "spring-boot:run"]