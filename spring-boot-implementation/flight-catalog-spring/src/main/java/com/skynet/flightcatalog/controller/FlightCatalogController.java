package com.skynet.flightcatalog.controller;

import com.skynet.flightcatalog.model.Flight;
import com.skynet.flightcatalog.service.FlightCatalogService;
import com.skynet.flightcatalog.dto.FlightRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/flights")
@CrossOrigin(origins = "*")
public class FlightCatalogController {
    
    @Autowired
    private FlightCatalogService flightCatalogService;
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    // ========================================
    // Functionality 1: Add/Update Flights
    // ========================================
    
    @PostMapping
    public ResponseEntity<?> addFlight(@RequestBody FlightRequestDTO flightDTO) {
        try {
            Flight flight = flightDTO.toFlight();
            Flight savedFlight = flightCatalogService.addFlight(flight);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedFlight);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to add flight: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFlight(@PathVariable Integer id, @RequestBody FlightRequestDTO flightDTO) {
        try {
            Flight flight = flightDTO.toFlight();
            Flight updatedFlight = flightCatalogService.updateFlight(id, flight);
            return ResponseEntity.ok(updatedFlight);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update flight: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFlight(@PathVariable Integer id) {
        try {
            flightCatalogService.deleteFlight(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to delete flight: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Flight> getFlightById(@PathVariable Integer id) {
        try {
            Flight flight = flightCatalogService.getFlightById(id);
            return ResponseEntity.ok(flight);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<Flight>> getAllFlights() {
        List<Flight> flights = flightCatalogService.getAllFlights();
        return ResponseEntity.ok(flights);
    }
    
    @GetMapping("/airline/{airlineId}")
    public ResponseEntity<List<Flight>> getFlightsByAirline(@PathVariable Integer airlineId) {
        List<Flight> flights = flightCatalogService.getFlightsByAirline(airlineId);
        return ResponseEntity.ok(flights);
    }
    
    // ========================================
    // Functionality 2: Search Flights by Date/Route
    // ========================================
    
    @GetMapping("/search/date")
    public ResponseEntity<List<Flight>> searchByDate(@RequestParam String date) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(date, FORMATTER);
            List<Flight> flights = flightCatalogService.searchFlightsByDate(dateTime);
            return ResponseEntity.ok(flights);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/search/route")
    public ResponseEntity<List<Flight>> searchByRoute(
            @RequestParam Integer depAirportId,
            @RequestParam Integer arrAirportId) {
        List<Flight> flights = flightCatalogService.searchFlightsByRoute(depAirportId, arrAirportId);
        return ResponseEntity.ok(flights);
    }
    
    @GetMapping("/search/route-date")
    public ResponseEntity<List<Flight>> searchByRouteAndDate(
            @RequestParam Integer depAirportId,
            @RequestParam Integer arrAirportId,
            @RequestParam String date) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(date, FORMATTER);
            List<Flight> flights = flightCatalogService.searchFlightsByRouteAndDate(depAirportId, arrAirportId, dateTime);
            return ResponseEntity.ok(flights);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // ========================================
    // Functionality 3: Filter by Price/Duration
    // ========================================
    
    @GetMapping("/filter/price/max")
    public ResponseEntity<List<Flight>> filterByMaxPrice(@RequestParam Double maxPrice) {
        List<Flight> flights = flightCatalogService.filterFlightsByMaxPrice(maxPrice);
        return ResponseEntity.ok(flights);
    }
    
    @GetMapping("/filter/price/range")
    public ResponseEntity<List<Flight>> filterByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {
        List<Flight> flights = flightCatalogService.filterFlightsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(flights);
    }
    
    @GetMapping("/filter/duration/max")
    public ResponseEntity<List<Flight>> filterByMaxDuration(@RequestParam Long maxDurationMinutes) {
        List<Flight> flights = flightCatalogService.filterFlightsByMaxDuration(maxDurationMinutes);
        return ResponseEntity.ok(flights);
    }
    
    @GetMapping("/filter/duration/range")
    public ResponseEntity<List<Flight>> filterByDurationRange(
            @RequestParam Long minDurationMinutes,
            @RequestParam Long maxDurationMinutes) {
        List<Flight> flights = flightCatalogService.filterFlightsByDurationRange(minDurationMinutes, maxDurationMinutes);
        return ResponseEntity.ok(flights);
    }
    
    // ========================================
    // Functionality 4: Update Flight Status
    // ========================================
    
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Integer id, @RequestParam String status) {
        try {
            Flight flight = flightCatalogService.updateFlightStatus(id, status);
            return ResponseEntity.ok(flight);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update status: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Flight>> getFlightsByStatus(@PathVariable String status) {
        List<Flight> flights = flightCatalogService.getFlightsByStatus(status);
        return ResponseEntity.ok(flights);
    }
    
    // ========================================
    // Enhancement 2: Sort Flights
    // ========================================
    
    @PostMapping("/sort/departure-time")
    public ResponseEntity<List<Flight>> sortByDepartureTime(@RequestBody List<Flight> flights) {
        List<Flight> sorted = flightCatalogService.sortFlightsByDepartureTime(flights);
        return ResponseEntity.ok(sorted);
    }
    
    @PostMapping("/sort/lowest-price")
    public ResponseEntity<List<Flight>> sortByLowestPrice(@RequestBody List<Flight> flights) {
        List<Flight> sorted = flightCatalogService.sortFlightsByLowestPrice(flights);
        return ResponseEntity.ok(sorted);
    }
    
    @PostMapping("/sort/shortest-duration")
    public ResponseEntity<List<Flight>> sortByShortestDuration(@RequestBody List<Flight> flights) {
        List<Flight> sorted = flightCatalogService.sortFlightsByShortestDuration(flights);
        return ResponseEntity.ok(sorted);
    }
    
    // ========================================
    // Enhancement 3: Duplicate Detection
    // ========================================
    
    @GetMapping("/check-duplicate")
    public ResponseEntity<Map<String, Boolean>> checkDuplicate(
            @RequestParam Integer depAirportId,
            @RequestParam Integer arrAirportId,
            @RequestParam String date,
            @RequestParam Integer airlineId) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(date, FORMATTER);
            boolean isDuplicate = flightCatalogService.isDuplicateFlight(depAirportId, arrAirportId, dateTime, airlineId);
            Map<String, Boolean> result = new HashMap<>();
            result.put("isDuplicate", isDuplicate);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Boolean> result = new HashMap<>();
            result.put("isDuplicate", false);
            return ResponseEntity.ok(result);
        }
    }
}
