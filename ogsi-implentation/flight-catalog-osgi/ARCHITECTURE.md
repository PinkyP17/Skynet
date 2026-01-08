# Flight Catalog OSGi Bundle - Architecture Documentation

## 📐 Architecture Overview

### **OSGi Bundle Architecture**

```
┌─────────────────────────────────────────────────────────┐
│              OSGi Framework (Apache Felix)              │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  ┌──────────────────────────────────────────────────┐ │
│  │     Flight Catalog Bundle (This Bundle)          │ │
│  ├──────────────────────────────────────────────────┤ │
│  │                                                   │ │
│  │  ┌──────────────────────────────────────────┐   │ │
│  │  │  Exported Packages (Public API)          │   │ │
│  │  │  - com.skynet.flightcatalog.osgi.api     │   │ │
│  │  │  - com.skynet.flightcatalog.osgi.model   │   │ │
│  │  └──────────────────────────────────────────┘   │ │
│  │                                                   │ │
│  │  ┌──────────────────────────────────────────┐   │ │
│  │  │  Private Packages (Hidden)               │   │ │
│  │  │  - com.skynet.flightcatalog.osgi.impl    │   │ │
│  │  │  - com.skynet.flightcatalog.osgi.dao     │   │ │
│  │  └──────────────────────────────────────────┘   │ │
│  │                                                   │ │
│  │  ┌──────────────────────────────────────────┐   │ │
│  │  │  OSGi Service Registry                   │   │ │
│  │  │  - FlightCatalogService (registered)     │   │ │
│  │  └──────────────────────────────────────────┘   │ │
│  │                                                   │ │
│  └──────────────────────────────────────────────────┘ │
│                                                         │
│  ┌──────────────────────────────────────────────────┐ │
│  │  Other Bundles (can add more)                    │ │
│  └──────────────────────────────────────────────────┘ │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

---

## 🏗️ Component Architecture

### **Layer Structure**

```
┌─────────────────────────────────────┐
│   OSGi Service Registry              │
│   (Service Discovery)                 │
└──────────────┬────────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│   FlightCatalogService (Interface)   │
│   - Public API Contract              │
└──────────────┬────────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│   FlightCatalogServiceImpl           │
│   - Business Logic                   │
│   - Validation                       │
│   - Enhancement Logic                 │
└──────────────┬────────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│   FlightDao                          │
│   - Database Operations              │
│   - SQL Queries                      │
│   - Result Mapping                   │
└──────────────┬────────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│   SQLite Database                    │
│   - applicationDataBase.db           │
└─────────────────────────────────────┘
```

---

## 📦 Package Structure

### **Exported Packages (Public)**

These packages are visible to other bundles:

1. **`com.skynet.flightcatalog.osgi.api`**
   - `FlightCatalogService` - Service interface
   - Other bundles can import and use this interface

2. **`com.skynet.flightcatalog.osgi.model`**
   - `Flight` - Flight data model
   - `Airport` - Airport data model
   - Other bundles can use these models

### **Private Packages (Hidden)**

These packages are not visible to other bundles:

1. **`com.skynet.flightcatalog.osgi.impl`**
   - `FlightCatalogServiceImpl` - Service implementation
   - Business logic details

2. **`com.skynet.flightcatalog.osgi.dao`**
   - `FlightDao` - Database access
   - SQL query details

3. **`com.skynet.flightcatalog.osgi`**
   - `Activator` - Bundle lifecycle management

---

## 🔄 Service Registration Flow

```
1. Bundle Started
   │
   ▼
2. Activator.start() called
   │
   ▼
3. Create FlightCatalogServiceImpl
   │
   ▼
4. Register service in OSGi Service Registry
   │
   ▼
5. Other bundles can discover and use service
```

---

## 🔌 How Other Bundles Use This Service

```java
// In another bundle
BundleContext context = ...; // Get from Activator

// Discover service
ServiceReference<FlightCatalogService> ref = 
    context.getServiceReference(FlightCatalogService.class);

// Get service instance
FlightCatalogService service = context.getService(ref);

// Use service
List<Flight> flights = service.getAllFlights();
Flight flight = service.getFlightById(1);
```

---

## 🗄️ Database Access Pattern

```
FlightDao
  │
  ├── ensureStatusColumnExists()  // Migration
  ├── getConnection()              // SQLite connection
  ├── findById()                   // Read one
  ├── findAll()                    // Read all
  ├── save()                       // Create
  ├── update()                     // Update
  ├── delete()                     // Delete
  ├── findByDate()                 // Search
  ├── findByRoute()                // Search
  ├── findByStatus()               // Filter
  └── isDuplicate()                // Validation
```

---

## ✅ Key Design Decisions

1. **Service Interface Pattern**: Program to interfaces, not implementations
2. **Package Visibility**: Export only API, hide implementation
3. **Database Migration**: Auto-add status column for backward compatibility
4. **Error Handling**: Return null/throw exceptions appropriately
5. **Logging**: Console logging for debugging

---

## 🔐 Security Considerations

- Database file should be accessible to the bundle
- No authentication/authorization (assumes trusted environment)
- SQL injection protection via PreparedStatement

---

## 📊 Performance Considerations

- In-process calls (fast, no network overhead)
- Direct database access (no ORM overhead)
- Connection pooling not implemented (simple use case)

---

## 🧪 Testing Strategy

1. **Unit Tests**: Test service methods with mock DAO
2. **Integration Tests**: Test with real database
3. **Bundle Tests**: Test bundle lifecycle in OSGi framework

---

## 🔄 Lifecycle Management

```
Bundle Lifecycle:
  INSTALLED → RESOLVED → STARTING → ACTIVE → STOPPING → RESOLVED → UNINSTALLED
                              │
                              ▼
                    Service Registered
                              │
                              ▼
                    Other bundles can use service
```

---

## 📝 Dependencies

- **OSGi Core API**: Bundle framework
- **SQLite JDBC**: Database driver
- **SLF4J**: Logging (optional)

---

## 🎯 Benefits of This Architecture

1. ✅ **Modularity**: Separate, independent bundle
2. ✅ **Hot-Swapping**: Update without restart
3. ✅ **Loose Coupling**: Interface-based communication
4. ✅ **Service Discovery**: Dynamic service registry
5. ✅ **Encapsulation**: Private implementation details
6. ✅ **In-Process**: Fast, no network overhead
