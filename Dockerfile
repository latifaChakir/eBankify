# Étape 1 : Construction de l'application
FROM maven:3.8.6-eclipse-temurin-17 AS build
LABEL authors="Youcode"

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests
FROM eclipse-temurin:17-jre-jammy
LABEL authors="Youcode"

WORKDIR /app

# Copier le fichier JAR généré depuis l'étape de build
COPY --from=build /app/target/eBankify-0.0.1-SNAPSHOT.jar app.jar

# Exposer le port d'exécution
EXPOSE 8080

# Lancer l'application Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
