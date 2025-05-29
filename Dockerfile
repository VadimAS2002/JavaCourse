FROM gradle:8.4-jdk20 AS builder

WORKDIR /workspace

COPY build.gradle settings.gradle gradlew gradle/ ./

RUN gradle --no-daemon dependencies

COPY src src

RUN gradle --no-daemon clean bootJar -x test

FROM eclipse-temurin:20-jdk-jammy AS runtime
WORKDIR /app

COPY --from=builder  /workspace/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

FROM eclipse-temurin:20-jdk-jammy AS runtime
WORKDIR /app

COPY --from=builder  /workspace/build/libs/*.jar app.jar

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --retries=3 CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]