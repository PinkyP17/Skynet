# Skynet Flight Booking System - Developer Guide

This document provides an overview of the recent implementations for the Booking Module, covering the Spring Boot Microservices, OSGi Bundles, and the JavaFX Desktop Client integration.

## 🚀 What Has Been Done

### 1. Booking Core Module (Spring Boot)
**Location:** `spring-boot-implementation/booking-service`
*   **Implementation:** Developed a fully functional REST API for flight bookings.
*   **Features:**
    *   **Create Booking:** reserves seats, validates flight/passenger existence, and generates a unique PNR.
    *   **Cancel Booking:** updates status to `CANCELLED`.
    *   **Retrieve PNR:** fetches the 6-character alphanumeric reference code.
    *   **Inter-Service Communication:** Uses `RestTemplate` to verify passenger existence with the `passenger-profile-service`.
*   **Testing:** Comprehensive Unit and Integration tests using Mockito and MockMvc.

### 2. OSGi Modular Implementation
**Location:** `ogsi-implentation/booking-osgi` & `passenger-ogsi`
*   **Modularity:** Wrapped business logic into OSGi bundles to demonstrate modular architecture.
*   **Services:**
    *   `BookingService`: Exported interface for booking operations.
    *   `PassengerProfileService`: Exported interface for passenger management.
*   **Deployment:** Bundles are configured to auto-deploy in Apache Felix.

### 3. JavaFX Client Integration
**Location:** `src/main/java/controller/PaymentPageController.java` & `util/RestClient.java`
*   **Hybrid Architecture:** The desktop client now operates in a hybrid mode.
*   **Logic:**
    *   Attempts to create bookings via the **Spring Boot Booking Service** (Port 8082) first.
    *   **Fallback:** If the service is offline, it seamlessly reverts to the local JDBC DAO (`ReservationDao`) implementation.
*   **Refactoring:** `RestClient` was refactored to manage multiple service endpoints (`/api/passenger-profile` and `/api/bookings`).

---

## 🛠️ How to Run the System

### Prerequisites
*   Java JDK 17 or higher
*   Maven 3.6+

### Step 1: Start the Microservices
You need to run the backend services for the hybrid features to work.

**1. Passenger Profile Service (Port 8081)**
```bash
cd spring-boot-implementation/passenger-spring
mvn spring-boot:run
```

**2. Booking Service (Port 8082)**
```bash
cd spring-boot-implementation/booking-service
mvn spring-boot:run
```

### Step 2: Run the JavaFX Application
Once the services are up (optional, but recommended for full features), run the desktop app.

```bash
# From the project root
mvn javafx:run
```
*Navigate to a flight, select a seat, and proceed to payment. Check the console logs to see if the booking was made via "Spring Boot Service" or "DAO Fallback".*

### Step 3: Run the OSGi Container (Alternative Architecture)
To test the modular OSGi implementation:

```bash
cd felix-framework/felix-framework-7.0.5
java -jar bin/felix.jar
```
*   **Verify Bundles:** Type `lb` to see `booking-osgi-bundle` and `passenger-profile-osgi-bundle` in `Active` state.
*   **Verify Services:** Type `services` to see the registered service interfaces.

---

## 🧪 Running Tests

**Booking Service Tests:**
```bash
mvn test -f spring-boot-implementation/booking-service/pom.xml
```

**Verify OSGi Bundles:**
```bash
mvn clean package -f ogsi-implentation/booking-osgi/pom.xml
mvn clean package -f ogsi-implentation/passenger-ogsi/pom.xml
```

---

## 🔧 Troubleshooting

*   **Database Locked:** Since SQLite is a file-based database, running the JavaFX app and Spring Boot services simultaneously might cause lock contentions if they write at the exact same time. The implementations use the same physical DB file (`src/main/resources/dataBase/applicationDataBase.db`).
*   **Service Not Found:** Ensure ports `8081` and `8082` are free before starting the Spring Boot apps.
*   **Compilation Errors:** If you modify `RestClient`, ensure all dependent controllers (`PersonalInformationController`, `PaymentPageController`) are updated to match the method signatures.
