# Passenger Profile OSGi Bundle - Complete Guide

## 🎯 What We've Built

An OSGi bundle that demonstrates **runtime modularity** for the Passenger Profile module.

---

## 📦 Bundle Structure

```
passenger-profile-osgi-bundle/
├── pom.xml                          # Maven configuration with Felix plugin
├── src/main/java/
│   └── com/skynet/passengerprofile/osgi/
│       ├── api/
│       │   └── PassengerProfileService.java      # Service interface (exported)
│       ├── impl/
│       │   └── PassengerProfileServiceImpl.java  # Service implementation (private)
│       ├── model/
│       │   └── Passenger.java                    # Data model (private)
│       ├── dao/
│       │   └── PassengerDao.java                 # Database access (private)
│       ├── client/
│       │   └── PassengerProfileClient.java       # Example usage (private)
│       └── Activator.java                        # Bundle lifecycle manager
└── target/
    └── passenger-profile-osgi-bundle-1.0.0.jar   # Built bundle
```

---

## 🏗️ OSGi Architecture Explained

### **Key OSGi Concepts:**

#### **1. Bundle**
- A module packaged as a JAR file with OSGi metadata
- Can be installed, started, stopped, updated, uninstalled at runtime
- Our bundle: `passenger-profile-osgi-bundle-1.0.0.jar`

#### **2. Service Registry**
- Central place where bundles register and discover services
- Loose coupling: bundles don't directly reference each other
- Communication pattern:
  ```
  Bundle A → registers service → Service Registry
  Bundle B → discovers service ← Service Registry
  Bundle B → uses service → Bundle A (via interface)
  ```

#### **3. Bundle Activator**
- Manages bundle lifecycle
- `start()` - called when bundle is activated
- `stop()` - called when bundle is deactivated

#### **4. Exported vs Private Packages**
- **Exported** (`Export-Package`): Visible to other bundles
  - `com.skynet.passengerprofile.osgi.api` - Service interface
- **Private** (`Private-Package`): Hidden from other bundles
  - `com.skynet.passengerprofile.osgi.impl` - Implementation
  - `com.skynet.passengerprofile.osgi.model` - Data models
  - `com.skynet.passengerprofile.osgi.dao` - Database access

---

## 🚀 Building the Bundle

### **Step 1: Copy Database**

Make sure `applicationDataBase.db` is in the project root:

```bash
cp /path/to/Skynet/applicationDataBase.db passenger-profile-osgi-bundle/
```

### **Step 2: Build Bundle**

```bash
cd passenger-profile-osgi-bundle
mvn clean package
```

**Output:**
```
[INFO] Building bundle passenger-profile-osgi-bundle-1.0.0
[INFO] BUILD SUCCESS
```

**Result:** Creates `target/passenger-profile-osgi-bundle-1.0.0.jar`

---

## 🧪 Testing the Bundle with Apache Felix

### **Option 1: Download Felix Framework**

1. **Download Apache Felix:**
   - Go to: https://felix.apache.org/downloads.cgi
   - Download: `org.apache.felix.main.distribution-7.0.5.zip`
   - Extract to a folder (e.g., `felix-framework`)

2. **Start Felix:**
   ```bash
   cd felix-framework
   java -jar bin/felix.jar
   ```

   **You'll see:**
   ```
   ____________________________
   Welcome to Apache Felix Gogo

   g!
   ```

3. **Install Your Bundle:**
   ```bash
   g! install file:///path/to/passenger-profile-osgi-bundle-1.0.0.jar
   Bundle ID: 5
   ```

4. **Start Your Bundle:**
   ```bash
   g! start 5
   ```

   **You'll see:**
   ```
   ========================================
   🚀 Passenger Profile Bundle STARTING...
   ========================================
   ✅ PassengerProfileService registered in OSGi Service Registry
      Service ID: 42
      Bundle ID: 5
      Bundle Name: passenger-profile-osgi-bundle
      Bundle Version: 1.0.0
   
   📋 Available Service Methods:
      - getPassenger(Long id)
      - updatePassenger(Long id, Passenger passenger)
      - getProfileCompleteness(Long id)
      ...
   
   🎯 Other bundles can now discover and use this service!
   ========================================
   ```

5. **Check Bundle Status:**
   ```bash
   g! lb
   ```

   **Output:**
   ```
   START LEVEL 1
      ID|State      |Level|Name
      0|Active     |    0|System Bundle (7.0.5)
      1|Active     |    1|Apache Felix Bundle Repository (2.0.10)
      2|Active     |    1|Apache Felix Gogo Command (1.1.2)
      3|Active     |    1|Apache Felix Gogo Runtime (1.1.6)
      4|Active     |    1|Apache Felix Gogo Shell (1.1.4)
      5|Active     |    1|passenger-profile-osgi-bundle (1.0.0)  ← YOUR BUNDLE!
   ```

6. **List Registered Services:**
   ```bash
   g! services
   ```

   **Look for:**
   ```
   {com.skynet.passengerprofile.osgi.api.PassengerProfileService}={service.id=42}
   Registered by bundle: passenger-profile-osgi-bundle [5]
   ```

7. **Stop Bundle (Hot Undeployment):**
   ```bash
   g! stop 5
   ```

   **You'll see:**
   ```
   ========================================
   🛑 Passenger Profile Bundle STOPPING...
   ========================================
   ✅ PassengerProfileService unregistered
   👋 Passenger Profile Bundle stopped gracefully
   ========================================
   ```

8. **Start Bundle Again (Hot Redeployment):**
   ```bash
   g! start 5
   ```

   **Bundle starts again without restarting Felix!**

9. **Update Bundle (Hot Swapping):**
   ```bash
   # After making changes and rebuilding
   g! update 5 file:///path/to/new-version.jar
   ```

   **Bundle is replaced at runtime!**

10. **Uninstall Bundle:**
    ```bash
    g! uninstall 5
    ```

---

## 🎓 Key OSGi Features Demonstrated

### **1. Service Registry Pattern**

**Traditional (Tight Coupling):**
```java
// Bundle A directly creates Bundle B's class
PassengerService service = new PassengerServiceImpl();
service.getPassenger(1L);
```

**OSGi (Loose Coupling):**
```java
// Bundle A discovers service via registry
ServiceReference<PassengerProfileService> ref = 
    context.getServiceReference(PassengerProfileService.class);
PassengerProfileService service = context.getService(ref);
service.getPassenger(1L);
```

### **2. Runtime Modularity**

- **Install:** Add bundle while system is running
- **Start/Stop:** Activate/deactivate modules on-the-fly
- **Update:** Replace bundle with new version (hot-swap)
- **Uninstall:** Remove bundle completely

### **3. Version Management**

```xml
<!-- In MANIFEST.MF (auto-generated by maven-bundle-plugin) -->
Bundle-Version: 1.0.0
Export-Package: com.skynet.passengerprofile.osgi.api;version="1.0.0"
Import-Package: com.skynet.passengerprofile.osgi.api;version="[1.0,2.0)"
```

- Bundles can specify version ranges they support
- Multiple versions can coexist in the same JVM!

### **4. Classloader Isolation**

- Each bundle has its own classloader
- Private packages are truly private
- No classpath pollution

---

## 📊 Architecture Comparison

### **Monolithic (Before):**
```
Single Application (One JAR, One JVM)
├── Passenger Profile Module
├── Flight Catalog Module
└── Booking Module

Problems:
- Tightly coupled
- Must restart entire app for updates
- All modules share same classloader
```

### **Spring Boot (Microservices):**
```
Passenger Profile Service (Port 8081, Process 1)
Flight Catalog Service (Port 8082, Process 2)
Booking Service (Port 8083, Process 3)

Communication: REST APIs over HTTP

Benefits:
- Independent deployment
- Technology independence
- Scalable

Drawbacks:
- Network overhead
- More complex infrastructure
- Must restart service for updates
```

### **OSGi (Dynamic Modules):**
```
Single JVM, Multiple Bundles
├── Passenger Profile Bundle (can start/stop/update)
├── Flight Catalog Bundle (can start/stop/update)
└── Booking Bundle (can start/stop/update)

Communication: Service Registry (in-memory)

Benefits:
- Hot-swapping (no restart!)
- In-process communication (fast)
- Modular but still one app

Drawbacks:
- More complex than monolith
- Requires OSGi framework
- Learning curve
```

---

## 🎯 For Your Report

### **What to Highlight:**

#### **1. Componentization Achieved:**
- Module packaged as independent bundle
- Can be deployed/undeployed independently
- Loose coupling via Service Registry

#### **2. Runtime Modularity:**
- Install bundle while app is running
- Update bundle without restarting app
- Stop/start modules on-demand

#### **3. Service-Oriented:**
- Interface-based programming
- Service discovery via registry
- Version management

#### **4. Classloader Isolation:**
- Private packages truly private
- No interference between bundles
- Multiple versions can coexist

---

## 📸 Screenshots to Capture

For your report, capture:

1. **Felix starting your bundle** - Shows bundle activation
2. **`lb` command output** - Shows bundle in ACTIVE state
3. **`services` command** - Shows registered service
4. **`stop` command** - Shows bundle stopping gracefully
5. **`start` command** - Shows hot redeployment
6. **`update` command** - Shows hot-swapping (if you make changes)

---

## 🔄 Hot-Swapping Demo Script

To demonstrate hot-swapping for your presentation:

```bash
# 1. Start Felix and install bundle
g! install file:///path/to/bundle.jar
g! start 5

# 2. Modify code (e.g., change log message in Activator)
# Edit Activator.java: Change "Bundle STARTING" to "Bundle STARTING v2"

# 3. Rebuild
mvn clean package

# 4. Update bundle (hot-swap!)
g! update 5 file:///path/to/new-bundle.jar

# Output shows new message without restarting Felix!
```

---

## ✅ Testing Checklist

- [ ] Built bundle successfully (`mvn clean package`)
- [ ] Downloaded and extracted Apache Felix
- [ ] Started Felix framework
- [ ] Installed bundle (`install file://...`)
- [ ] Started bundle (`start <id>`)
- [ ] Checked bundle status (`lb`)
- [ ] Listed services (`services`)
- [ ] Stopped bundle (`stop <id>`)
- [ ] Started bundle again (`start <id>`)
- [ ] Updated bundle (`update <id>`)
- [ ] Uninstalled bundle (`uninstall <id>`)

---

## 🎓 Summary

**You now have:**
- ✅ OSGi bundle for Passenger Profile module
- ✅ Service Registry integration
- ✅ Runtime deployment capability
- ✅ Hot-swapping demonstration
- ✅ Complete componentization (different from Spring Boot)

**Next Steps:**
- Test the bundle with Felix
- Capture screenshots for report
- Add enhancements (completeness meter, etc.)
- Compare all 3 architectures in report

---

**Ready to test! Download Felix and try it out!** 🚀
