FROM eclipse-temurin:21.0.7_6-jdk-alpine AS builder

WORKDIR /usr/app

COPY . .

RUN ./gradlew clean bootJar --no-daemon

FROM eclipse-temurin:21.0.7_6-jdk-alpine

COPY --from=builder /usr/app/build/libs/application.jar /opt/app/application.jar

RUN addgroup -S app && adduser -S app -G app
USER app:app

ARG STORAGE_LOCATION=/srv/drive/storage
ENV STORAGE_LOCATION=$STORAGE_LOCATION

RUN mkdir -p $STORAGE_LOCATION && chown -R app:app $STORAGE_LOCATION

EXPOSE 80

CMD ["sh", "-c", "java -jar /opt/app/application.jar"]