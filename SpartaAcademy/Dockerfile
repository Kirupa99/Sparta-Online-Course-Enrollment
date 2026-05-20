# Build stage
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY .. .
RUN mvn -B -DskipTests clean package

# Run stage
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8091
ENV PORT=8091
CMD ["sh", "-c", "java -jar app.jar --spring.profiles.active=prod --server. Port=${PORT}"]