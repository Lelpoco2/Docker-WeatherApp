# Dockerfile per Spring Boot Weather App

# Fase 1: Build dell'applicazione
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Imposta la directory di lavoro
WORKDIR /app

# Copia i file di configurazione Maven
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn

# Scarica le dipendenze (layer cache)
RUN mvn dependency:go-offline -B

# Copia il codice sorgente
COPY src ./src

# Esegui il build dell'applicazione
RUN mvn clean package -DskipTests

# Fase 2: Runtime dell'applicazione
FROM eclipse-temurin:17-jre-alpine

# Installa curl per health check (usando apk su Alpine)
RUN apk add --no-cache curl

# Crea un utente non-root per sicurezza
RUN addgroup --system spring && adduser --system spring --ingroup spring

# Imposta la directory di lavoro
WORKDIR /app

# Copia il JAR dall'immagine di build
COPY --from=build /app/target/weatherapp-*.jar app.jar

# Cambia ownership del file
RUN chown spring:spring app.jar

# Cambia all'utente non-root
USER spring:spring

# Esponi la porta dell'applicazione
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Comando per avviare l'applicazione
ENTRYPOINT ["java", "-jar", "app.jar"]
