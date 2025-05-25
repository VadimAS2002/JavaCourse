FROM gradle:8.4-jdk17 AS builder

RUN echo "nameserver 8.8.8.8" > /etc/resolv.conf
RUN echo "nameserver 8.8.4.4" >> /etc/resolv.conf

WORKDIR /workspace

COPY build.gradle settings.gradle gradlew gradle/ ./

RUN gradle --no-daemon dependencies

COPY src src

RUN gradle --no-daemon clean bootJar -x test

FROM eclipse-temurin:17-jdk-jammy AS runtime
WORKDIR /app

COPY --from=builder  /workspace/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]