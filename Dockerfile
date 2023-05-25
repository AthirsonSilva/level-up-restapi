FROM eclipse-temurin:20-jdk

WORKDIR /app

COPY . /app

RUN ./mvnw clean package -DskipTests

COPY target/*.jar /app/app.jar

ENV DATABASE_URL=jdbc:postgresql://next-spring-db.cnfncmvuylgz.sa-east-1.rds.amazonaws.com:5432/next-spring
ENV DATABASE_USERNAME=postgres
ENV DATABASE_PASSWORD=39EAbPorUL9QTsKd6Rhg

EXPOSE 8000

ENTRYPOINT ["java", "-jar", "app.jar"]