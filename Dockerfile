FROM openjdk:11.0.8-slim-buster

COPY . /app
WORKDIR /app
RUN bash mvnw clean install
RUN mv ./target/*.jar ./app.jar

EXPOSE 5021

ENTRYPOINT ["java", "-jar", "app.jar", "-Duser.timezone=Europe/London"]
