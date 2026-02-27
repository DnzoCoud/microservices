FROM gradle:9-jdk21 AS build
WORKDIR /app

# Cache deps
COPY build.gradle settings.gradle gradle.properties* ./
COPY gradle ./gradle
RUN gradle --no-daemon dependencies || true

# Build
COPY src ./src
RUN gradle --no-daemon bootJar

# ===== Run stage =====
FROM eclipse-temurin:21-jre
WORKDIR /app

RUN useradd -ms /bin/bash appuser
USER appuser

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]