# Flight Catalog Spring Boot Implementation - Complete Documentation

## 📋 Overview

This document provides comprehensive documentation for the **Spring Boot implementation** of the Flight Catalog module for the Skynet Flight Booking System. This implementation demonstrates a **microservice architecture** approach, where the Flight Catalog functionality is exposed as a RESTful web service that can be consumed by any client application.

## Module: Flight Catalog

**Author:** Rahimi  
**Version:** 1.0.0  
**Technology:** Spring Boot 3.x  
**Service Port:** 8082  
**Base URL:** `http://localhost:8082/api/flight-catalog`

---

## 🎯 Key Benefits of Spring Boot Implementation

### **1. Microservice Architecture** 🚀
- **Independent Service:** Runs as a separate process, completely decoupled from the main application
- **Scalability:** Can be deployed on multiple servers for load balancing
- **Technology Flexibility:** Can be consumed by any client (JavaFX, web apps, mobile apps, etc.)
- **Service Isolation:** Failures in one service don't crash the entire system

### **2. RESTful API Design** 🌐
- **Standard HTTP Protocol:** Uses well-established HTTP methods (GET, POST, PUT, DELETE)
- **Language Agnostic:** Any programming language can consume the API
- **Easy Integration:** Simple HTTP requests, no complex dependencies
- **API Documentation:** Can be easily documented with Swagger/OpenAPI

### **3. Spring Framework Benefits** ⚙️
- **Dependency Injection:** Automatic dependency management and loose coupling
- **Spring Data JPA:** Simplified database operations with minimal boilerplate code
- **Transaction Management:** Automatic transaction handling for data consistency
- **Error Handling:** Built-in exception handling with proper HTTP status codes
- **Configuration Management:** Externalized configuration via `application.properties`

### **4. Production-Ready Features** 🏭
- **Health Checks:** Built-in actuator endpoints for monitoring
- **Logging:** Comprehensive logging framework integration
- **Security:** Easy to add Spring Security for authentication/authorization
- **Testing:** Excellent support for unit and integration testing
- **Deployment:** Can be containerized with Docker, deployed to cloud platforms

### **5. Developer Experience** 👨‍💻
- **Auto-Configuration:** Minimal configuration required, sensible defaults
- **Hot Reload:** Development tools for quick iteration
- **IDE Support:** Excellent IntelliJ IDEA and Eclipse support
- **Maven Integration:** Standard build tool, easy dependency management

---

## 📁 Project Structure

```
flight-catalog-spring/
├── pom.xml                                    # Maven dependencies & build config
├── README.md                                  # Quick start guide
├── SPRING_BOOT_IMPLEMENTATION.md              # This comprehensive documentation
├── run-service.bat                            # Windows batch script to run service
└── src/main/
    ├── java/com/skynet/flightcatalog/
    │   ├── FlightCatalogApplication.java      # Main Spring Boot application
    │   │
    │   ├── config/                            # Configuration classes
    │   │   ├── JacksonConfig.java             # JSON serialization config
    │   │   └── SqliteDateTimeConverter.java   # Custom JPA converter for dates
    │   │
    │   ├── controller/                        # REST API layer
    │   │   └── FlightCatalogController.java   # @RestController - HTTP endpoints
    │   │
    │   ├── service/                            # Business logic layer
    │   │   └── FlightCatalogService.java      # @Service - Business operations
    │   │
    │   ├── repository/                        # Data access layer
    │   │   └── FlightRepository.java          # @Repository - JPA repository
    │   │
    │   ├── model/                             # Domain models
    │   │   └── Flight.java                    # @Entity - JPA entity
    │   │
    │   └── dto/                                # Data Transfer Objects
    │       └── FlightRequestDTO.java          # DTO for request payloads
    │
    └── resources/
        └── application.properties             # Service configuration
```

---

## 🏗️ Architecture

### **Layered Architecture Pattern**

```
┌─────────────────────────────────────────────────────────────┐
│                    Client Applications                       │
│  (JavaFX App, Web App, Mobile App, Postman, etc.)           │
└──────────────────────┬──────────────────────────────────────┘
                       │ HTTP/REST
                       │ (JSON)
┌──────────────────────▼──────────────────────────────────────┐
│              Controller Layer (@RestController)             │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  FlightCatalogController                            │   │
│  │  - Handles HTTP requests                            │   │
│  │  - Validates input                                  │   │
│  │  - Returns HTTP responses                           │   │
│  └──────────────────────────────────────────────────────┘   │
└──────────────────────┬──────────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────────┐
│              Service Layer (@Service)                        │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  FlightCatalogService                               │   │
│  │  - Business logic                                    │   │
│  │  - Validation rules                                  │   │
│  │  - Transaction management                            │   │
│  │  - Duplicate detection                              │   │
│  └──────────────────────────────────────────────────────┘   │
└──────────────────────┬──────────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────────┐
│           Repository Layer (@Repository)                     │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  FlightRepository (Spring Data JPA)                  │   │
│  │  - Database operations                              │   │
│  │  - Custom queries                                   │   │
│  │  - Automatic CRUD methods                          │   │
│  └──────────────────────────────────────────────────────┘   │
└──────────────────────┬──────────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────────┐
│                    Database (SQLite)                         │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  applicationDataBase.db                              │   │
│  │  - flights table                                    │   │
│  │  - Shared with main application                     │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

### **Key Architectural Components**

#### **1. Controller Layer (`FlightCatalogController`)**
- **Responsibility:** Handle HTTP requests and responses
- **Annotations:**
  - `@RestController` - Marks as REST controller
  - `@RequestMapping("/flights")` - Base path for all endpoints
  - `@CrossOrigin` - Allows cross-origin requests
- **HTTP Methods:**
  - `GET` - Retrieve data
  - `POST` - Create new resources
  - `PUT` - Update existing resources
  - `DELETE` - Remove resources

#### **2. Service Layer (`FlightCatalogService`)**
- **Responsibility:** Business logic and validation
- **Features:**
  - Duplicate flight detection
  - Status validation
  - Price/duration calculations
  - Sorting logic
- **Transaction Management:** Automatic via Spring `@Transactional`

#### **3. Repository Layer (`FlightRepository`)**
- **Responsibility:** Database operations
- **Technology:** Spring Data JPA
- **Features:**
  - Automatic CRUD methods
  - Custom query methods using `@Query`
  - Method name-based queries
- **Custom Queries:**
  - Search by date/route
  - Filter by price/duration
  - Duplicate detection queries

#### **4. Model Layer (`Flight`)**
- **Responsibility:** Domain entity representation
- **Annotations:**
  - `@Entity` - JPA entity
  - `@Table(name = "flights")` - Database table mapping
  - `@Id`, `@GeneratedValue` - Primary key
  - `@Column` - Column mappings
  - `@Convert` - Custom converters for dates
- **Helper Methods:**
  - `getMinPrice()` - Calculate minimum price
  - `getDurationMinutes()` - Calculate flight duration
  - `getStatusColor()` - Get color for status indicator

#### **5. Configuration Layer**
- **JacksonConfig:** Custom JSON serialization for `LocalDateTime`
- **SqliteDateTimeConverter:** Custom JPA converter for SQLite date handling
- **application.properties:** Service configuration (port, database, etc.)

---

## 🔌 API Endpoints

### **Base URL:** `http://localhost:8082/api/flight-catalog`

### **Flight Management**

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| `GET` | `/flights` | Get all flights | - |
| `GET` | `/flights/{id}` | Get flight by ID | - |
| `GET` | `/flights/airline/{airlineId}` | Get flights by airline | - |
| `POST` | `/flights` | Create new flight | `FlightRequestDTO` |
| `PUT` | `/flights/{id}` | Update flight | `FlightRequestDTO` |
| `DELETE` | `/flights/{id}` | Delete flight | - |

### **Search Endpoints**

| Method | Endpoint | Description | Query Parameters |
|--------|----------|-------------|------------------|
| `GET` | `/flights/search/date` | Search by date | `date` (yyyy-MM-dd HH:mm) |
| `GET` | `/flights/search/route` | Search by route | `depAirportId`, `arrAirportId` |
| `GET` | `/flights/search/route-date` | Search by route and date | `depAirportId`, `arrAirportId`, `date` |

### **Filter Endpoints**

| Method | Endpoint | Description | Query Parameters |
|--------|----------|-------------|------------------|
| `GET` | `/flights/filter/price/max` | Filter by max price | `maxPrice` |
| `GET` | `/flights/filter/price/range` | Filter by price range | `minPrice`, `maxPrice` |
| `GET` | `/flights/filter/duration/max` | Filter by max duration | `maxDurationMinutes` |
| `GET` | `/flights/filter/duration/range` | Filter by duration range | `minDurationMinutes`, `maxDurationMinutes` |

### **Status Management**

| Method | Endpoint | Description | Query Parameters |
|--------|----------|-------------|------------------|
| `PUT` | `/flights/{id}/status` | Update flight status | `status` (On Time/Delayed/Cancelled) |
| `GET` | `/flights/status/{status}` | Get flights by status | - |

### **Sorting Endpoints**

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| `POST` | `/flights/sort/departure-time` | Sort by departure time | `List<Flight>` |
| `POST` | `/flights/sort/lowest-price` | Sort by lowest price | `List<Flight>` |
| `POST` | `/flights/sort/shortest-duration` | Sort by shortest duration | `List<Flight>` |

### **Duplicate Detection**

| Method | Endpoint | Description | Query Parameters |
|--------|----------|-------------|------------------|
| `GET` | `/flights/check-duplicate` | Check for duplicate flight | `depAirportId`, `arrAirportId`, `date`, `airlineId` |
| `GET` | `/flights/check-duplicate-excluding` | Check duplicate excluding ID | `depAirportId`, `arrAirportId`, `date`, `airlineId`, `excludeId` |

---

## 📝 Request/Response Examples

### **Create Flight (POST /flights)**

**Request:**
```json
{
  "airlineId": 1,
  "depAirportId": 2,
  "arrAirportId": 3,
  "depDatetime": "2026-01-30 13:00",
  "arrDatetime": "2026-01-30 15:30",
  "firstPrice": 500.0,
  "businessPrice": 300.0,
  "economyPrice": 150.0,
  "luggagePrice": 50.0,
  "weightPrice": 10.0,
  "status": "On Time"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "airlineId": 1,
  "depAirportId": 2,
  "arrAirportId": 3,
  "depDatetime": "2026-01-30T13:00:00",
  "arrDatetime": "2026-01-30T15:30:00",
  "firstPrice": 500.0,
  "businessPrice": 300.0,
  "economyPrice": 150.0,
  "luggagePrice": 50.0,
  "weightPrice": 10.0,
  "status": "On Time"
}
```

**Error Response (400 Bad Request - Duplicate):**
```json
{
  "error": "Duplicate flight detected: A flight with the same route, date, and airline already exists."
}
```

### **Search by Route (GET /flights/search/route)**

**Request:**
```
GET /flights/search/route?depAirportId=2&arrAirportId=3
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "airlineId": 1,
    "depAirportId": 2,
    "arrAirportId": 3,
    "depDatetime": "2026-01-30T13:00:00",
    "arrDatetime": "2026-01-30T15:30:00",
    "firstPrice": 500.0,
    "businessPrice": 300.0,
    "economyPrice": 150.0,
    "luggagePrice": 50.0,
    "weightPrice": 10.0,
    "status": "On Time"
  }
]
```

---

## ⚙️ Configuration

### **application.properties**

```properties
# Server Configuration
server.port=8082
server.servlet.context-path=/api/flight-catalog

# Database Configuration
spring.datasource.url=jdbc:sqlite:D:/UM DEGREE/SEM 7/CBSE GA/AA/Skynet/src/main/resources/dataBase/applicationDataBase.db
spring.datasource.driver-class-name=org.sqlite.JDBC

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.community.dialect.SQLiteDialect

# Logging Configuration
logging.level.com.skynet.flightcatalog=INFO
logging.level.org.springframework.web=INFO
```

### **Key Configuration Points:**

1. **Port:** Service runs on port 8082
2. **Context Path:** All endpoints prefixed with `/api/flight-catalog`
3. **Database:** Uses shared SQLite database with main application
4. **JPA:** Auto-updates schema, uses SQLite dialect
5. **Logging:** Configured for INFO level

---

## 🔄 Integration with Main Application

### **Integration Architecture**

```
┌─────────────────────────────────────────────────────────────┐
│              Main JavaFX Application                         │
│  ┌──────────────────────────────────────────────────────┐  │
│  │  DashboardController                                  │  │
│  │  - UI for airline users                              │  │
│  │  - Flight management interface                       │  │
│  └──────────────────────┬───────────────────────────────┘  │
│                          │                                   │
│  ┌──────────────────────▼───────────────────────────────┐  │
│  │  FlightCatalogRestClient                             │  │
│  │  - HTTP client for Spring Boot service               │  │
│  │  - Handles GET/POST/PUT/DELETE requests             │  │
│  │  - JSON serialization/deserialization                │  │
│  └──────────────────────┬───────────────────────────────┘  │
└──────────────────────────┼──────────────────────────────────┘
                           │ HTTP/REST
                           │ (localhost:8082)
┌──────────────────────────▼──────────────────────────────────┐
│         Spring Boot Flight Catalog Service                   │
│  ┌──────────────────────────────────────────────────────┐  │
│  │  FlightCatalogController                              │  │
│  │  - REST API endpoints                                 │  │
│  │  - JSON request/response                              │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

### **Integration Components**

#### **1. FlightCatalogRestClient**
- **Location:** `src/main/java/util/FlightCatalogRestClient.java`
- **Purpose:** HTTP client wrapper for Spring Boot service
- **Features:**
  - Service availability check
  - GET/POST/PUT/DELETE methods
  - Error handling
  - JSON conversion

#### **2. FlightConverter**
- **Location:** `src/main/java/util/FlightConverter.java`
- **Purpose:** Convert between main app `Flight` model and Spring Boot JSON
- **Features:**
  - `fromJsonNode()` - Convert JSON to Flight
  - `toJsonString()` - Convert Flight to JSON

#### **3. FlightDTO**
- **Location:** `src/main/java/util/FlightDTO.java`
- **Purpose:** Data Transfer Object for API requests
- **Features:**
  - Handles date string conversion
  - Maps to Spring Boot `FlightRequestDTO`

### **Fallback Mechanism**

The main application includes a **fallback mechanism**:
1. **Primary:** Try to use Spring Boot service via REST client
2. **Fallback:** If service unavailable, use local `FlightDao`
3. **Seamless:** User experience remains consistent

```java
// Pseudo-code from DashboardController
if (restClient.isServiceAvailable()) {
    // Use Spring Boot service
    restClient.post("/flights", flightDTO);
} else {
    // Fallback to local DAO
    flightDao.create(flight);
}
```

---

## 🎨 Key Features & Enhancements

### **✅ Functionality Requirements**

#### **1. Add/Update Flights**
- ✅ Create new flights via `POST /flights`
- ✅ Update existing flights via `PUT /flights/{id}`
- ✅ Delete flights via `DELETE /flights/{id}`
- ✅ Get flight by ID via `GET /flights/{id}`
- ✅ Get all flights via `GET /flights`

#### **2. Search Flights by Date/Route**
- ✅ Search by date: `GET /flights/search/date?date={date}`
- ✅ Search by route: `GET /flights/search/route?depAirportId={id}&arrAirportId={id}`
- ✅ Search by route and date: `GET /flights/search/route-date?depAirportId={id}&arrAirportId={id}&date={date}`

#### **3. Filter by Price/Duration**
- ✅ Filter by max price: `GET /flights/filter/price/max?maxPrice={amount}`
- ✅ Filter by price range: `GET /flights/filter/price/range?minPrice={min}&maxPrice={max}`
- ✅ Filter by max duration: `GET /flights/filter/duration/max?maxDurationMinutes={minutes}`
- ✅ Filter by duration range: `GET /flights/filter/duration/range?minDurationMinutes={min}&maxDurationMinutes={max}`

#### **4. Update Flight Status**
- ✅ Update status: `PUT /flights/{id}/status?status={status}`
- ✅ Get flights by status: `GET /flights/status/{status}`
- ✅ Status values: "On Time", "Delayed", "Cancelled"

### **✅ Enhancement Requirements**

#### **1. Flight Status Color Indicator**
- ✅ `getStatusColor()` method in `Flight` model
- ✅ Returns hex color codes:
  - 🟢 Green (#4CAF50): "On Time"
  - 🔴 Red (#F44336): "Delayed"
  - ⚫ Grey (#9E9E9E): "Cancelled"
- ✅ Used by UI for visual status display

#### **2. Sort Flights**
- ✅ Sort by departure time: `POST /flights/sort/departure-time`
- ✅ Sort by lowest price: `POST /flights/sort/lowest-price`
- ✅ Sort by shortest duration: `POST /flights/sort/shortest-duration`
- ✅ Helper methods: `getMinPrice()`, `getDurationMinutes()`

#### **3. Duplicate Flight Detection**
- ✅ Check duplicate: `GET /flights/check-duplicate`
- ✅ Check duplicate excluding ID: `GET /flights/check-duplicate-excluding`
- ✅ Prevents adding flights with same route, date, and airline
- ✅ Returns clear error messages

---

## 🚀 Running the Service

### **Option 1: Using Maven (Command Line)**

```bash
cd spring-boot-implementation/flight-catalog-spring
mvn spring-boot:run
```

### **Option 2: Using Batch Script (Windows)**

```bash
# Double-click or run:
run-service.bat
```

### **Option 3: Build and Run JAR**

```bash
# Build
mvn clean package

# Run
java -jar target/flight-catalog-service-1.0.0.jar
```

### **Option 4: Using IDE**

1. Open `flight-catalog-spring` folder in IntelliJ IDEA or Eclipse
2. Wait for Maven to download dependencies
3. Run `FlightCatalogApplication.java` as Java Application

### **Verifying Service is Running**

```bash
# Check if service is running
curl http://localhost:8082/api/flight-catalog/flights

# Or open in browser
http://localhost:8082/api/flight-catalog/flights
```

---

## 🔍 Testing the API

### **Using cURL**

```bash
# Get all flights
curl http://localhost:8082/api/flight-catalog/flights

# Create a flight
curl -X POST http://localhost:8082/api/flight-catalog/flights \
  -H "Content-Type: application/json" \
  -d '{
    "airlineId": 1,
    "depAirportId": 2,
    "arrAirportId": 3,
    "depDatetime": "2026-01-30 13:00",
    "arrDatetime": "2026-01-30 15:30",
    "firstPrice": 500.0,
    "businessPrice": 300.0,
    "economyPrice": 150.0,
    "luggagePrice": 50.0,
    "weightPrice": 10.0,
    "status": "On Time"
  }'

# Search by route
curl "http://localhost:8082/api/flight-catalog/flights/search/route?depAirportId=2&arrAirportId=3"

# Update status
curl -X PUT "http://localhost:8082/api/flight-catalog/flights/1/status?status=Delayed"
```

### **Using Postman**

1. Import collection or create requests manually
2. Base URL: `http://localhost:8082/api/flight-catalog`
3. Set Content-Type: `application/json` for POST/PUT requests
4. Use query parameters for GET requests

---

## 🆚 Comparison: Spring Boot vs OSGi

| Aspect | Spring Boot | OSGi |
|-------|-------------|------|
| **Architecture** | Microservice (separate process) | Modular (same JVM) |
| **Communication** | HTTP/REST (network) | Method calls (in-process) |
| **Output Format** | JSON | Java objects |
| **Scalability** | Horizontal (multiple instances) | Vertical (single JVM) |
| **Deployment** | Independent service | Bundle in framework |
| **Technology Stack** | Spring Framework | OSGi Framework |
| **Use Case** | Web services, cloud, distributed | Desktop apps, plugins |
| **Testing** | HTTP client, Postman | Direct method calls |
| **Port** | 8082 | N/A |
| **Dependencies** | Spring Boot, JPA, Jackson | OSGi Core, SQLite JDBC |

### **When to Use Spring Boot:**
- ✅ Building web services or APIs
- ✅ Need to support multiple client types
- ✅ Want to scale horizontally
- ✅ Need cloud deployment
- ✅ Building microservices architecture

### **When to Use OSGi:**
- ✅ Building desktop applications
- ✅ Need hot-swapping capabilities
- ✅ Want in-process modularity
- ✅ Plugin-based architecture
- ✅ Single JVM deployment

---

## 🛠️ Technical Details

### **Dependencies (pom.xml)**

```xml
<!-- Spring Boot Starter -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Spring Data JPA -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- SQLite JDBC -->
<dependency>
    <groupId>org.xerial</groupId>
    <artifactId>sqlite-jdbc</artifactId>
</dependency>

<!-- SQLite Dialect -->
<dependency>
    <groupId>org.hibernate.orm</groupId>
    <artifactId>hibernate-community-dialects</artifactId>
</dependency>

<!-- Jackson for JSON -->
<dependency>
    <groupId>com.fasterxml.jackson.datatype</groupId>
    <artifactId>jackson-datatype-jsr310</artifactId>
</dependency>
```

### **Custom Date Handling**

**Problem:** SQLite doesn't natively support `LocalDateTime`

**Solution:**
1. **SqliteDateTimeConverter:** Custom JPA converter to convert `LocalDateTime` ↔ `String`
2. **JacksonConfig:** Custom JSON serialization to use `yyyy-MM-dd HH:mm` format
3. **FlightRequestDTO:** DTO that accepts dates as strings

### **Database Schema**

The service uses the existing `flights` table:
- `id` (INTEGER PRIMARY KEY)
- `dep_datetime` (TEXT)
- `arr_datetime` (TEXT)
- `first_price` (REAL)
- `business_price` (REAL)
- `economy_price` (REAL)
- `luggage_price` (REAL)
- `weight_price` (REAL)
- `id_airline` (INTEGER)
- `dep_airport` (INTEGER)
- `arr_airport` (INTEGER)
- `status` (TEXT) - Added for status feature

---

## 📊 Benefits Summary

### **For Development:**
- ✅ **Rapid Development:** Spring Boot auto-configuration reduces boilerplate
- ✅ **Standard Patterns:** Follows industry-standard REST API patterns
- ✅ **Easy Testing:** Can test with HTTP clients, Postman, or unit tests
- ✅ **Documentation:** Easy to document with Swagger/OpenAPI

### **For Deployment:**
- ✅ **Independent Deployment:** Can deploy separately from main app
- ✅ **Scalability:** Can run multiple instances behind load balancer
- ✅ **Cloud Ready:** Easy to deploy to AWS, Azure, Google Cloud
- ✅ **Containerization:** Can be containerized with Docker

### **For Integration:**
- ✅ **Language Agnostic:** Any language can consume the API
- ✅ **Multiple Clients:** Web, mobile, desktop apps can all use it
- ✅ **API Gateway:** Can be integrated with API gateways
- ✅ **Versioning:** Easy to version APIs (v1, v2, etc.)

### **For Maintenance:**
- ✅ **Separation of Concerns:** Clear layer separation
- ✅ **Error Handling:** Standard HTTP status codes
- ✅ **Logging:** Comprehensive logging support
- ✅ **Monitoring:** Can add Spring Boot Actuator for metrics

---

## 🎓 Learning Outcomes

### **Spring Boot Concepts Demonstrated:**
- ✅ **RESTful API Design:** Standard HTTP methods and status codes
- ✅ **Spring Data JPA:** Repository pattern, custom queries
- ✅ **Dependency Injection:** `@Autowired`, `@Service`, `@Repository`
- ✅ **Configuration Management:** `application.properties`
- ✅ **Exception Handling:** `@ExceptionHandler`, HTTP status codes
- ✅ **DTO Pattern:** Request/Response DTOs for API contracts
- ✅ **Custom Converters:** JPA converters for SQLite compatibility

### **Microservice Concepts:**
- ✅ **Service Independence:** Separate process, independent deployment
- ✅ **API Contracts:** Well-defined REST API endpoints
- ✅ **Data Serialization:** JSON for data exchange
- ✅ **Service Discovery:** Can be extended with service registry
- ✅ **Load Balancing:** Can run multiple instances

---

## 🔒 Important Notes

### **Database Sharing**
- The Spring Boot service shares the same SQLite database with the main application
- Both implementations can coexist and use the same data
- Database path must be configured correctly in `application.properties`

### **Port Configuration**
- Default port: **8082**
- Can be changed in `application.properties`
- If changed, update `FlightCatalogRestClient.BASE_URL` in main app

### **Date Format**
- API uses format: `yyyy-MM-dd HH:mm` (e.g., "2026-01-30 13:00")
- This is handled by `JacksonConfig` and `FlightRequestDTO`

### **Status Values**
- Valid statuses: "On Time", "Delayed", "Cancelled"
- Case-sensitive in database queries
- Default: "On Time"

---

## 📚 Additional Resources

- **Spring Boot Documentation:** https://spring.io/projects/spring-boot
- **Spring Data JPA:** https://spring.io/projects/spring-data-jpa
- **REST API Best Practices:** https://restfulapi.net/
- **SQLite Documentation:** https://www.sqlite.org/docs.html

---

## 👤 Author

**Rahimi**  
Flight Catalog Module - Spring Boot Implementation

---

## 📅 Implementation Date

January 2025

---

## ✅ Verification Checklist

- [x] Service builds successfully
- [x] All functionality requirements implemented
- [x] All enhancement requirements implemented
- [x] REST API endpoints working
- [x] Database connection configured
- [x] Integration with main app working
- [x] Fallback mechanism implemented
- [x] Error handling with proper HTTP status codes
- [x] Documentation complete
- [x] Date format handling correct
- [x] Duplicate detection working
- [x] Status color coding implemented

---

**End of Documentation**
