
FROM openjdk:17-jdk-alpine as stage1

WORKDIR /app

COPY gradlew .

COPY gradle gradle
COPY src src
COPY build.gradle .
COPY settings.gradle .

RUN chmod 777 gradlew
RUN ./gradlew bootJar

# 두 번째 스테이지
FROM openjdk:17-jdk-alpine
WORKDIR /app

COPY --from=stage1 /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]