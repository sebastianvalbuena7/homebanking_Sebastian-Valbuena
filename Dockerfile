FROM gradle:7.6.0-jdk19-alpine

COPY . .

RUN gradle build

EXPOSE 8080