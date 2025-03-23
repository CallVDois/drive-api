FROM amazoncorretto:21-alpine AS builder

WORKDIR /app

COPY . .

RUN ./gradlew clean :infrastructure:bootJar --no-daemon

FROM amazoncorretto:21-alpine

WORKDIR /app

RUN mkdir -p /app/storage && chmod -R 777 /app/storage

COPY --from=builder /app/build/libs/application.jar app.jar

ENV SPRING_PROFILES_ACTIVE=local
ENV KEYCLOAK_REALM=callv2
ENV KEYCLOAK_HOST=http://localhost:8090
ENV POSTGRES_HOST=localhost
ENV POSTGRES_PORT=5432
ENV POSTGRES_DATABASE=callv2
ENV POSTGRES_USERNAME=callv2
ENV POSTGRES_PASSWORD=callv2
ENV STORAGE_LOCATION=/app/storage

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -jar /app/app.jar --spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]