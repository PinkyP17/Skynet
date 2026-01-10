# Flight Catalog Spring Boot Service

## Quick Start

### Option 1: Using Maven (if installed)
```bash
mvn spring-boot:run
```

### Option 2: Using IDE (Recommended)
1. Open the `flight-catalog-spring` folder in IntelliJ IDEA or Eclipse
2. Wait for Maven to download dependencies
3. Run `FlightCatalogApplication.java` as a Java Application

### Option 3: Build and Run JAR
```bash
# Build the JAR
mvn clean package

# Run the JAR
java -jar target/flight-catalog-service-1.0.0.jar
```

### Option 4: Using Batch Script
Double-click `run-service.bat` (will check for Maven and provide instructions if not found)

## Service Configuration

- **Port:** 8082
- **Base URL:** http://localhost:8082/api/flight-catalog
- **Database:** Uses the main application database at `D:/UM DEGREE/SEM 7/CBSE GA/AA/Skynet/src/main/resources/dataBase/applicationDataBase.db`

## API Endpoints

### Flights
- `GET /flights` - Get all flights
- `GET /flights/{id}` - Get flight by ID
- `GET /flights/airline/{airlineId}` - Get flights by airline
- `POST /flights` - Add new flight
- `PUT /flights/{id}` - Update flight
- `DELETE /flights/{id}` - Delete flight

### Search
- `GET /flights/search/date?date={yyyy-MM-dd HH:mm}` - Search by date
- `GET /flights/search/route?depAirportId={id}&arrAirportId={id}` - Search by route
- `GET /flights/search/route-date?depAirportId={id}&arrAirportId={id}&date={yyyy-MM-dd HH:mm}` - Search by route and date

### Filters
- `GET /flights/filter/price/max?maxPrice={amount}` - Filter by max price
- `GET /flights/filter/price/range?minPrice={min}&maxPrice={max}` - Filter by price range
- `GET /flights/filter/duration/max?maxDurationMinutes={minutes}` - Filter by max duration
- `GET /flights/filter/duration/range?minDurationMinutes={min}&maxDurationMinutes={max}` - Filter by duration range

### Status
- `PUT /flights/{id}/status?status={On Time|Delayed|Cancelled}` - Update flight status
- `GET /flights/status/{status}` - Get flights by status

### Sort
- `POST /flights/sort/departure-time` - Sort by departure time
- `POST /flights/sort/lowest-price` - Sort by lowest price
- `POST /flights/sort/shortest-duration` - Sort by shortest duration

### Duplicate Detection
- `GET /flights/check-duplicate?depAirportId={id}&arrAirportId={id}&date={yyyy-MM-dd HH:mm}&airlineId={id}` - Check for duplicate

## Troubleshooting

### Maven Not Found
If you see "mvn is not recognized":
1. Install Maven from https://maven.apache.org/download.cgi
2. Add Maven bin directory to your PATH environment variable
3. Or use an IDE (IntelliJ IDEA/Eclipse) to run the application

### Port Already in Use
If port 8082 is already in use:
1. Change `server.port` in `src/main/resources/application.properties`
2. Update the main app's `FlightCatalogRestClient.java` BASE_URL accordingly

### Database Connection Error
Make sure the database path in `application.properties` is correct and the database file exists.
