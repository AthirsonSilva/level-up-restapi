# next-spring
Game rental service API done with Spring Boot to test my knowledge of this framework

## Requirements

- [Java 21](https://adoptium.net/)
- [Maven](https://maven.apache.org/)
- [Gradle](https://gradle.org/)

## Architecture Layers

## Project Features

✔️ Consistent API design

✔️ Use of DTOs (Data Transfer Objects)

✔️ Use of HATEOS (Hypertext as the Engine of Application State)

✔️ Spring Security with JWT (Json Web Token)

✔️ User authentication with email verification

✔️ Caching with Redis (Coming soon)

✔️ Documentation with SwaggerUI

✔️ Pagination and sorting 

✔️ Mailing service with JavaMail

✔️ Data generation with JavaFaker

✔️ Layered architecture

✔️ Global Error Handling

✔️ Testing with JUnit (In development)

✔️ API versioning

✔️ File upload 

✔️ Logging with Log4j2 (In development)

✔️ Use rate limiting (Coming soon)

✔️ Circuit breakers (Coming soon)

✔️ CI/ CD with Docker and Railway

#### PRESENTATION LAYER

This layer is at the top of the architecture. This tier is responsible for:

✔️ Performing authentication.

✔️ Converting JSON data into an object (and vice versa).

✔️ Handling HTTP requests.

✔️ Transfering authentication to the business layer.

#### BUSINESS LAYER

The business layer is responsible for:

✔️ Performing validation.

✔️ Performing authorization.

✔️ Handling the business logic and rules.

#### PERSISTENCE LAYER

This layer is responsible for:

✔️ Containing storage logic.

✔️ Fetching objects and translating them into database rows (and vice versa).

#### DATABASE LAYER

This layer is simply the actual database that is responsible for:

✔️ Performing database operations (mainly CRUD operations).

## Software structure

![image](https://github.com/AthirsonSilva/blog-api/assets/84593887/046588ab-6449-43f3-b68b-ed5c580146d9)

## Getting Started

1. Clone the repository
2. Navigate to the project directory
3. Build the project using Maven:

```
./mvnw clean package -DskipTests
```

4. Run the project using Maven wrapper:

```
./mvnw spring-boot:run
```

5. The application will start on http://localhost:8000

## Usage

- You will also need the database installed on your machine and set the connection vars on the application.properties or application.yml
