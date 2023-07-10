FROM eclipse-temurin:17-jdk-focal

LABEL version="0.0.2"
ARG IMAGE_TAG="0.0.2"
LABEL tag="${IMAGE_TAG}"
 
WORKDIR /app
 
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
 
COPY src ./src
 
CMD ["./mvnw", "spring-boot:run"]