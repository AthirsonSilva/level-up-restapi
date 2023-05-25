#
# Build stage
#
FROM maven:3.9.1-eclipse-temurin-20 AS build

COPY src /home/app/src

COPY pom.xml /home/app

RUN mvn -f /home/app/pom.xml clean package -DskipTests

#
# Package stage
#
FROM eclipse-temurin:20-jdk

COPY --from=build /home/app/target/*.jar /usr/local/lib/app.jar

EXPOSE 8000

ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]
