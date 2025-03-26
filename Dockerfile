# Étape 1 : Construire l'application avec Maven + JDK 21
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src

COPY wait-for-mysql.sh .
RUN chmod +x wait-for-mysql.sh
ENTRYPOINT ["./wait-for-mysql.sh", "mysql", "java", "-jar", "app.jar"]

RUN mvn clean package -DskipTests

# Étape 2 : Exécuter l'application avec JDK 21
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
