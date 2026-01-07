# Gemini Configuration for Passenger Profile Service (Inner Module)

## Project Context
This is the **Passenger Profile Service** module (located in the subdirectory) for the **Skynet Flight Booking System**. It handles passenger data, loyalty accounts, favorite routes, and travel documents.

## Technology Stack
- **Language**: Java 17
- **Framework**: Spring Boot 3.2.1
- **Database**: SQLite (using `sqlite-jdbc` and Hibernate dialects)
- **Utilities**: Lombok
- **Build Tool**: Maven

## Architecture & Conventions
The project follows a standard Spring Boot layered architecture:
- **Model**: JPA Entities located in `com.skynet.passengerprofile.model`.
- **Repository**: Data access interfaces extending `JpaRepository` in `com.skynet.passengerprofile.repository`.
- **Service**: Business logic in `com.skynet.passengerprofile.service`.
- **Controller**: REST API endpoints in `com.skynet.passengerprofile.controller`.

## Development Guidelines
1. **Lombok Usage**: Prefer Lombok annotations.
2. **Database**: SQLite.
3. **REST API**: Follow RESTful conventions.
4. **Testing**: Use `spring-boot-starter-test`.
