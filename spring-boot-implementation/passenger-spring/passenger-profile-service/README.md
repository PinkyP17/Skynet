# Passenger Profile Service - Spring Boot Module

## 📋 What We've Created So Far

### ✅ Phase 1 Complete: Project Setup & Entity Layer

**Files Created:**

1. **`pom.xml`** - Maven configuration with all dependencies
2. **`application.properties`** - Database and server configuration
3. **`PassengerProfileApplication.java`** - Main Spring Boot application class
4. **Entity Classes (Models):**
   - `Passenger.java` - Main passenger profile
   - `TravelDocument.java` - Travel documents with expiry alerts
   - `LoyaltyAccount.java` - Loyalty points and tier system
   - `FavoriteRoute.java` - Saved favorite routes

---

## 🚀 Next Steps

### Phase 2: Create Repository Layer (CRUD Operations)
### Phase 3: Create Service Layer (Business Logic)
### Phase 4: Create Controller Layer (REST APIs)
### Phase 5: Testing

---

## 📁 Project Structure

```
passenger-profile-service/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/skynet/passengerprofile/
│   │   │   ├── PassengerProfileApplication.java
│   │   │   ├── model/
│   │   │   │   ├── Passenger.java
│   │   │   │   ├── TravelDocument.java
│   │   │   │   ├── LoyaltyAccount.java
│   │   │   │   └── FavoriteRoute.java
│   │   │   ├── repository/ (TO CREATE NEXT)
│   │   │   ├── service/ (TO CREATE NEXT)
│   │   │   └── controller/ (TO CREATE NEXT)
│   │   └── resources/
│   │       ├── application.properties
│   │       └── applicationDataBase.db (COPY FROM SKYNET PROJECT)
│   └── test/
```

---

## 🔧 Setup Instructions

### Step 1: Create the Project Structure

1. **Create a new folder** named `passenger-profile-service`
2. **Copy all the files** I created into this structure
3. **Copy your database file** (`applicationDataBase.db`) from the Skynet project to:
   ```
   passenger-profile-service/src/main/resources/applicationDataBase.db
   ```

### Step 2: Update application.properties

Open `application.properties` and update the database path:

```properties
# Update this line with the correct path to your database
spring.datasource.url=jdbc:sqlite:src/main/resources/applicationDataBase.db
```

### Step 3: Open in IntelliJ

1. **Open IntelliJ IDEA**
2. **File → Open** → Select the `passenger-profile-service` folder
3. **Trust the project**
4. **Wait for Maven to download dependencies** (bottom right corner)

### Step 4: Run Maven Install

In IntelliJ terminal:
```bash
mvn clean install
```

---

## 🎯 Features Implemented in Entities

### 1. Passenger Entity
- ✅ All basic profile fields
- ✅ **Enhancement: Profile Completeness Meter** (`calculateProfileCompleteness()` method)
- ✅ Relationships to documents, loyalty, and favorites

### 2. TravelDocument Entity
- ✅ Document management (passport, visa, ID, license)
- ✅ **Enhancement: Document Expiry Alerts**
  - `isExpiringSoon()` - checks if expiring within 6 months
  - `isExpired()` - checks if already expired
  - `getDaysUntilExpiry()` - countdown days
  - `getExpiryStatus()` - returns "Expired", "Expiring Soon", or "Valid"

### 3. LoyaltyAccount Entity
- ✅ Points management (balance, earned, redeemed)
- ✅ **Enhancement: Loyalty Tier Visualizer**
  - `updateTier()` - auto-calculates tier based on points
  - `getProgressToNextTier()` - progress percentage (0-100)
  - `getPointsToNextTier()` - points needed for next tier
  - `getTierColor()` - color code for UI
  - `getTierBenefits()` - benefits description
- ✅ Tier thresholds: Silver (0-4999), Gold (5000-9999), Diamond (10000+)

### 4. FavoriteRoute Entity
- ✅ Save/manage favorite routes
- ✅ Custom nicknames for routes
- ✅ Helper methods for display and matching

---

## 🔑 Key Annotations Used

- `@Entity` - Marks class as JPA entity
- `@Table(name = "...")` - Maps to database table
- `@Id` - Primary key
- `@GeneratedValue` - Auto-increment
- `@Column(name = "...")` - Maps to database column
- `@ManyToOne` / `@OneToMany` / `@OneToOne` - Relationships
- `@JsonIgnore` - Prevents infinite loops in JSON
- `@Data` - Lombok: auto-generates getters/setters
- `@NoArgsConstructor` / `@AllArgsConstructor` - Lombok: constructors

---

## ⚙️ Application Configuration

**Server:** http://localhost:8081  
**Base URL:** http://localhost:8081/api/passenger-profile  
**Database:** SQLite (applicationDataBase.db)

---

## 📝 What's Next?

I'll create the Repository, Service, and Controller layers next. These will provide:

### Repository Layer (Data Access)
- CRUD operations for each entity
- Custom queries (find by email, search documents, etc.)

### Service Layer (Business Logic)
- Manage personal details
- Upload/manage travel documents
- Manage loyalty points and tiers
- Manage favorite routes
- Calculate profile completeness
- Alert for expiring documents

### Controller Layer (REST API)
- GET /passengers/{id} - Get passenger profile
- PUT /passengers/{id} - Update passenger
- POST /passengers/{id}/documents - Upload document
- GET /passengers/{id}/loyalty - Get loyalty account
- POST /passengers/{id}/favorites - Add favorite route
- And many more...

---

## 🤔 Ready for Next Phase?

Once you've:
1. ✅ Created the project structure
2. ✅ Copied the database file
3. ✅ Opened in IntelliJ
4. ✅ Run `mvn clean install` successfully

Let me know and I'll create the Repository, Service, and Controller layers!
