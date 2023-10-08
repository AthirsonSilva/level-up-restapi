# next-spring
Game rental service API done with Spring Boot to test my knowledge of this framework

## Contents

- [next-spring](#next-spring)
  - [Contents](#contents)
  - [Requirements](#requirements)
  - [Architecture Layers](#architecture-layers)
  - [Project Features](#project-features)
      - [PRESENTATION LAYER](#presentation-layer)
      - [BUSINESS LAYER](#business-layer)
      - [PERSISTENCE LAYER](#persistence-layer)
      - [DATABASE LAYER](#database-layer)
  - [Software structure](#software-structure)
  - [Getting Started](#getting-started)
  - [Usage](#usage)

## Requirements

- [Java 21](https://adoptium.net/)
- [Maven](https://maven.apache.org/)
- [Gradle](https://gradle.org/)

## Architecture Layers

## Project Features

| Feature                                    | Status           |
| ------------------------------------------ | ---------------- |
| Consistent API design                      | 🟢 Ready          |
| Use of DTOs                                | 🟢 Ready          |
| Use of HATEOAS                             | 🟢 Ready          |
| Spring Security with JWT                   | 🟢 Ready          |
| User authentication and email verification | 🟢 Ready          |
| Caching with Redis                         | 🔴 Not ready      |
| Documentation with SwaggerUI               | 🟢 Ready          |
| Pagination and sorting                     | 🟢 Ready          |
| Mailing service with JavaMail              | 🟢 Ready          |
| Data generation with JavaFaker             | 🟢 Ready          |
| Layered architecture                       | 🟢 Ready          |
| Global Error Handling                      | 🟢 Ready          |
| Testing with JUnit                         | 🟡 In development |
| API versioning                             | 🟢 Ready          |
| File upload and download                   | 🟡 In development |
| Logging with Log4j2                        | 🟡 In development |
| Use rate limiting                          | 🔴 Not ready      |
| Circuit breakers                           | 🔴 Not ready      |
| CI/ CD with Docker and Railway             | 🟢 Ready          |

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
