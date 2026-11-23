# ---------- Etape 1 : construire le JAR avec Maven ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn -B -q package -DskipTests

# ---------- Etape 2 : image finale (petite, Java seulement) ----------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Utilisateur NON root
RUN addgroup -S app && adduser -S app -G app \
    && mkdir -p /app/data \
    && chown -R app:app /app

COPY --from=build /build/target/*.jar /app/app.jar

USER app
EXPOSE 8080

# Docker verifie regulierement que l'application repond
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget -q -O /dev/null http://127.0.0.1:8080/health || exit 1

ENTRYPOINT ["java", "-jar", "/app/app.jar"]