# Use Maven to build the application
FROM maven:3-eclipse-temurin-17 AS build
# WORKDIR /app
COPY pom.xml .
# COPY src ./src
RUN mvn clean package -DskipTests

# Use OpenJDK to run the application
FROM eclipse-temurin:17-alpine
# WORKDIR /app
COPY --from=build /app/target/stock-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
