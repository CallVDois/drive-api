FROM eclipse-temurin:21.0.7_6-jdk-alpine AS builder

WORKDIR /usr/app

COPY . .

RUN ./gradlew clean bootJar --no-daemon

FROM eclipse-temurin:21.0.7_6-jre-alpine

COPY --from=builder /usr/app/build/libs/application.jar /opt/app/application.jar

RUN mkdir -p /var/log/drive-api
RUN mkdir -p /srv/drive/storage

RUN addgroup -S app && adduser -S app -G app
RUN chown -R app:app /srv/drive/storage
RUN chown -R app:app /var/log/drive-api

USER app:app

CMD ["sh", "-c", "java -jar /opt/app/application.jar"]