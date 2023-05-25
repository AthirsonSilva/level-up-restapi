# FROM eclipse-temurin:20-jdk

# WORKDIR /app

# COPY . /app

# RUN ./mvnw clean package -DskipTests

# COPY target/next-spring-1.jar /app/app.jar

# ENV DATABASE_URL=jdbc:postgresql://next-spring-db.cnfncmvuylgz.sa-east-1.rds.amazonaws.com:5432/next-spring
# ENV DATABASE_USERNAME=postgres
# ENV DATABASE_PASSWORD=39EAbPorUL9QTsKd6Rhg

# EXPOSE 8000

# ENTRYPOINT ["java", "-jar", "app.jar"]

#
# Build stage
#
FROM maven:3.9.1-eclipse-temurin-20 AS build

COPY src /home/app/src

COPY pom.xml /home/app

RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM eclipse-temurin:20-jdk

COPY --from=build /home/app/target/*.jar /usr/local/lib/app.jar

ENV DATABASE_URL=jdbc:postgresql://next-spring-db.cnfncmvuylgz.sa-east-1.rds.amazonaws.com:5432/next-spring
ENV DATABASE_USERNAME=postgres
ENV DATABASE_PASSWORD=39EAbPorUL9QTsKd6Rhg

EXPOSE 8000

ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]