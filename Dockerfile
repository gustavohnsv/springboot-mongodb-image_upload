FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package

FROM gcr.io/distroless/java17
WORKDIR /app
COPY --from=build /app/target/image-upload-0.0.1-SNAPSHOT.jar app.jar
COPY .env .env
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]