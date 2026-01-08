# Flight Catalog OSGi Bundle

## Overview

This is the **OSGi implementation** of the Flight Catalog module for the Skynet Flight Booking System. It is implemented as a separate OSGi bundle that can be dynamically loaded, started, stopped, and updated without restarting the entire application.

## Module: Flight Catalog

**Author:** Rahimi  
**Version:** 1.0.0  
**Technology:** OSGi (Apache Felix)

---

## 📋 Functionality Implemented

### **1. Add/Update Flights** ✅
- Create new flights
- Update existing flights
- Delete flights
- Get flight by ID
- Get all flights

### **2. Search Flights by Date/Route** ✅
- Search by departure date
- Search by route (departure & arrival airports)
- Search by route and date combined

### **3. Filter by Price/Duration** ✅
- Filter by maximum price
- Filter by price range
- Filter by maximum duration
- Filter by duration range

### **4. Update Flight Status** ✅
- Update flight status (ON_TIME, DELAYED, CANCELLED, BOARDING, DEPARTED, ARRIVED)
- Get flights by status

---

## 🚀 Enhancements Implemented

### **Enhancement 1: Flight Status Color Indicator** ✅
- Visual color coding for flight status
- Colors:
  - 🟢 Green: ON_TIME
  - 🟠 Orange: DELAYED
  - 🔴 Red: CANCELLED
  - 🔵 Blue: BOARDING
  - 🟣 Purple: DEPARTED
  - 🔷 Cyan: ARRIVED
- Method: `getStatusColor()` in Flight model

### **Enhancement 2: Sort Flights** ✅
- Sort by departure time (ascending/descending)
- Sort by lowest price (ascending/descending)
- Sort by shortest duration (ascending/descending)

### **Enhancement 3: Duplicate Flight Detection** ✅
- Prevents adding flights with the same route and date
- Works for both create and update operations
- Returns clear error messages

---

## 📁 Project Structure

```
flight-catalog-osgi/
├── pom.xml
├── README.md
└── src/main/java/com/skynet/flightcatalog/osgi/
    ├── Activator.java                    # Bundle lifecycle management
    ├── api/
    │   └── FlightCatalogService.java     # Service interface (exported)
    ├── impl/
    │   └── FlightCatalogServiceImpl.java  # Service implementation (private)
    ├── dao/
    │   └── FlightDao.java                # Database access (private)
    └── model/
        ├── Flight.java                   # Flight model (exported)
        └── Airport.java                  # Airport model (exported)
```

---

## 🏗️ Architecture

### **OSGi Bundle Structure**

```
OSGi Framework (Apache Felix)
├── System Bundle
├── Flight Catalog Bundle (THIS BUNDLE)
│   ├── Exported Packages:
│   │   ├── com.skynet.flightcatalog.osgi.api
│   │   └── com.skynet.flightcatalog.osgi.model
│   ├── Private Packages:
│   │   ├── com.skynet.flightcatalog.osgi.impl
│   │   └── com.skynet.flightcatalog.osgi.dao
│   └── Service Registry:
│       └── FlightCatalogService (registered)
└── Other Bundles (can add more)
```

### **Key OSGi Concepts Used**

1. **Bundle Activator**: Manages bundle lifecycle (start/stop)
2. **Service Registry**: Registers `FlightCatalogService` for other bundles to discover
3. **Exported Packages**: API and models visible to other bundles
4. **Private Packages**: Implementation details hidden from other bundles
5. **Service Interface**: Program to interfaces, not implementations

---

## 🔧 Building the Bundle

### **Prerequisites**
- Maven 3.6+
- Java 17+
- SQLite database file (`applicationDataBase.db`)

### **Step 1: Copy Database**

Copy the database file to the bundle root directory:

```bash
cp "D:\UM DEGREE\SEM 7\CBSE GA\AA\Skynet\src\main\resources\dataBase\applicationDataBase.db" "ogsi-implentation\flight-catalog-osgi\applicationDataBase.db"
```

### **Step 2: Build Bundle**

```bash
cd ogsi-implentation/flight-catalog-osgi
mvn clean package
```

**Output:** Creates `target/flight-catalog-osgi-bundle-1.0.0.jar`

---

## 🧪 Testing with Apache Felix

### **Step 1: Download Apache Felix**

1. Download from: https://felix.apache.org/downloads.cgi
2. Extract to a folder (e.g., `felix-framework`)

### **Step 2: Start Felix**

```bash
cd felix-framework
java -jar bin/felix.jar
```

You'll see:
```
Welcome to Apache Felix Gogo
g!
```

### **Step 3: Install Bundle**

**Option 1: If bundle is in Downloads (recommended for paths with spaces):**
```bash
g! install file:///C:/Users/msimo/Downloads/flight-catalog-osgi-bundle-1.0.0.jar
Bundle ID: 11
```

**Option 2: If using original path (must URL-encode spaces):**
```bash
g! install file:///D:/UM%20DEGREE/SEM%207/CBSE%20GA/AA/Skynet/ogsi-implentation/flight-catalog-osgi/target/flight-catalog-osgi-bundle-1.0.0.jar
```

**Note:** If you get file not found errors due to spaces in path, copy the bundle to Downloads folder first.

### **Step 4: Start Bundle**

```bash
g! start 11
```
The result:
```
========================================
🚀 Flight Catalog Bundle STARTING...
========================================
[FlightCatalogService] Service implementation created
✅ FlightCatalogService registered in OSGi Service Registry
   Service ID: 19
   Bundle ID: 11
   Bundle Name: flight-catalog-osgi-bundle
   Bundle Version: 1.0.0

📋 Available Service Methods:
   Functionality 1: Add/Update Flights
     - createFlight(Flight flight)
     - updateFlight(Integer id, Flight flight)
     - deleteFlight(Integer id)
     - getFlightById(Integer id)
     - getAllFlights()

   Functionality 2: Search Flights by Date/Route
     - searchFlightsByDate(LocalDateTime date)
     - searchFlightsByRoute(Integer depAirportId, Integer arrAirportId)
     - searchFlightsByRouteAndDate(...)

   Functionality 3: Filter by Price/Duration
     - filterFlightsByMaxPrice(Double maxPrice)
     - filterFlightsByPriceRange(Double minPrice, Double maxPrice)
     - filterFlightsByMaxDuration(Long maxDurationMinutes)
     - filterFlightsByDurationRange(...)

   Functionality 4: Update Flight Status
     - updateFlightStatus(Integer id, String status)
     - getFlightsByStatus(String status)

   Enhancements:
     - sortFlightsByDepartureTime(boolean ascending)
     - sortFlightsByPrice(boolean ascending)
     - sortFlightsByDuration(boolean ascending)
     - isDuplicateFlight(Flight flight, Integer excludeId)

🎯 Other bundles can now discover and use this service!
========================================
```

### **Step 5: Verify Bundle Status**

```bash
g! lb
```

You'll see your bundle listed:
```
START LEVEL 1
   ID|State      |Level|Name
    0|Active     |    0|System Bundle (7.0.5)|7.0.5
    1|Active     |    1|jansi (1.18.0)|1.18.0
    2|Active     |    1|JLine Bundle (3.13.2)|3.13.2
    3|Active     |    1|Apache Felix Bundle Repository (2.0.10)|2.0.10
    4|Active     |    1|Apache Felix Gogo Command (1.1.2)|1.1.2
    5|Active     |    1|Apache Felix Gogo JLine Shell (1.1.8)|1.1.8
    6|Active     |    1|Apache Felix Gogo Runtime (1.1.4)|1.1.4
   11|Active     |    1|Flight Catalog OSGi Bundle (1.0.0)|1.0.0
```

**Note:** Bundle ID 11 shows as "Active" - this means it's running successfully!

### **Step 6: Verify Service**

```bash
g! services FlightCatalogService
```

### **Step 6: Stop Bundle**

```bash
g! stop 11
```

---

## 📝 Service API

### **Functionality 1: Add/Update Flights**

```java
// Create flight
Flight flight = new Flight();
flight.setDepAirportId(1);
flight.setArrAirportId(2);
flight.setDepDatetime(LocalDateTime.now());
// ... set other fields
Flight created = service.createFlight(flight);

// Update flight
Flight updated = service.updateFlight(id, flight);

// Delete flight
boolean deleted = service.deleteFlight(id);

// Get flight
Flight flight = service.getFlightById(id);
List<Flight> allFlights = service.getAllFlights();
```

### **Functionality 2: Search Flights**

```java
// Search by date
List<Flight> flights = service.searchFlightsByDate(LocalDateTime.now());

// Search by route
List<Flight> flights = service.searchFlightsByRoute(1, 2);

// Search by route and date
List<Flight> flights = service.searchFlightsByRouteAndDate(1, 2, LocalDateTime.now());
```

### **Functionality 3: Filter Flights**

```java
// Filter by price
List<Flight> flights = service.filterFlightsByMaxPrice(500.0);
List<Flight> flights = service.filterFlightsByPriceRange(100.0, 500.0);

// Filter by duration
List<Flight> flights = service.filterFlightsByMaxDuration(600L); // 10 hours
List<Flight> flights = service.filterFlightsByDurationRange(60L, 600L);
```

### **Functionality 4: Update Status**

```java
// Update status
Flight flight = service.updateFlightStatus(1, "DELAYED");

// Get flights by status
List<Flight> delayedFlights = service.getFlightsByStatus("DELAYED");
```

### **Enhancements**

```java
// Sort flights
List<Flight> sorted = service.sortFlightsByDepartureTime(true);
List<Flight> sorted = service.sortFlightsByPrice(true);
List<Flight> sorted = service.sortFlightsByDuration(true);

// Check duplicate
boolean isDuplicate = service.isDuplicateFlight(flight, null);

// Get status color
String color = flight.getStatusColor(); // Returns hex color code
```

---

## 🔄 Database Schema

The bundle automatically adds a `status` column to the `flights` table if it doesn't exist (backward compatible).

**Flights Table:**
- `id` (INTEGER PRIMARY KEY)
- `dep_datetime` (TEXT)
- `arr_datetime` (TEXT)
- `first_price` (REAL)
- `business_price` (REAL)
- `economy_price` (REAL)
- `luggage_price` (REAL)
- `weight_price` (REAL)
- `dep_airport` (INTEGER)
- `arr_airport` (INTEGER)
- `id_airline` (INTEGER)
- `status` (TEXT) - **NEW COLUMN**

---

## ✅ Benefits of OSGi Implementation

1. **Modularity**: Flight Catalog is a separate, independent bundle
2. **Hot-Swapping**: Update bundle without restarting entire application
3. **Loose Coupling**: Other bundles use service interface, not implementation
4. **Service Registry**: Dynamic service discovery
5. **In-Process**: Fast, no network overhead
6. **Runtime Deployment**: Install/start/stop bundles at runtime

---

## 🆚 Comparison with Original Implementation

| Feature | Original | OSGi Implementation |
|---------|----------|-------------------|
| **Modularity** | Monolithic | Separate bundle |
| **Update** | Restart entire app | Hot-swap bundle |
| **Coupling** | Tight | Loose (via interface) |
| **Service Discovery** | N/A | OSGi Service Registry |
| **Deployment** | Static | Dynamic runtime |

---

## 📚 References

- [OSGi Specification](https://www.osgi.org/developer/specifications/)
- [Apache Felix Documentation](https://felix.apache.org/documentation/index.html)
- [Maven Bundle Plugin](https://felix.apache.org/documentation/subprojects/apache-felix-maven-bundle-plugin-bnd.html)

---

## 👤 Author

**Rahimi**  
Flight Catalog Module - OSGi Implementation
