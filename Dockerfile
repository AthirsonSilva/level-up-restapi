FROM eclipse-temurin:17-jdk

MAINTAINER "Athirson Silva"

WORKDIR /app

COPY /target/*.jar /app/app.jar

EXPOSE 8000

ENTRYPOINT ["java", "-jar", "app.jar"]