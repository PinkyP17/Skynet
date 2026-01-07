# Gemini Configuration for Passenger Profile Service

## Project Context
This is the **Passenger Profile Service** module for the **Skynet Flight Booking System**. It handles passenger data, loyalty accounts, favorite routes, and travel documents.

## Technology Stack
- **Language**: Java 21
- **Framework**: Spring Boot 3.2.1
- **Database**: SQLite (using `sqlite-jdbc` and Hibernate dialects)
- **Utilities**: Lombok (for reducing boilerplate code like getters/setters/constructors)
- **Build Tool**: Maven

## Architecture & Conventions
The project follows a standard Spring Boot layered architecture:
- **Model**: JPA Entities located in `com.skynet.passengerprofile.model`.
- **Repository**: Data access interfaces extending `JpaRepository` in `com.skynet.passengerprofile.repository`.
- **Service**: Business logic in `com.skynet.passengerprofile.service`.
- **Controller**: REST API endpoints in `com.skynet.passengerprofile.controller`.

## Development Guidelines
1. **Lombok Usage**: Prefer Lombok annotations (`@Data`, `@AllArgsConstructor`, `@NoArgsConstructor`, `@Builder`) over manual boilerplate.
2. **Database**: The project uses SQLite. Ensure migrations or schema changes are compatible.
3. **REST API**: Follow RESTful conventions for resource naming and HTTP methods.
4. **Testing**: Use `spring-boot-starter-test` for unit and integration tests.

## Key Files
- `pom.xml`: Dependency management.
- `src/main/resources/application.properties`: Configuration (database connection, server port).
