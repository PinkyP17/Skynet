package com.skynet.passengerprofile.controller;

import com.skynet.passengerprofile.model.Passenger;
import com.skynet.passengerprofile.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * PassengerController - REST API endpoints for Passenger Profile management
 * 
 * Base URL: /api/passenger-profile/passengers
 * 
 * Functionality 1: Manage Personal Details
 * Enhancement 1: Profile Completeness Meter
 */
@RestController
@RequestMapping("/passengers")
@CrossOrigin(origins = "*") // Allow CORS for testing
public class PassengerController {

    @Autowired
    private PassengerService passengerService;

    // ========================================
    // FUNCTIONALITY 1: MANAGE PERSONAL DETAILS
    // ========================================

    /**
     * Get all passengers
     * GET /passengers
     */
    @GetMapping
    public ResponseEntity<List<Passenger>> getAllPassengers() {
        List<Passenger> passengers = passengerService.getAllPassengers();
        return ResponseEntity.ok(passengers);
    }

    /**
     * Get passenger by ID
     * GET /passengers/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPassengerById(@PathVariable Long id) {
        Passenger passenger = passengerService.getPassengerById(id);
        
        if (passenger == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Passenger not found with ID: " + id));
        }
        
        return ResponseEntity.ok(passenger);
    }

    /**
     * Create new passenger
     * POST /passengers
     * Body: Passenger JSON
     */
    @PostMapping
    public ResponseEntity<?> createPassenger(@RequestBody Passenger passenger) {
        try {
            Passenger created = passengerService.createPassenger(passenger);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Update passenger
     * PUT /passengers/{id}
     * Body: Passenger JSON with fields to update
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePassenger(@PathVariable Long id, 
                                            @RequestBody Passenger passenger) {
        try {
            Passenger updated = passengerService.updatePassenger(id, passenger);
            
            if (updated == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Passenger not found with ID: " + id));
            }
            
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Delete passenger
     * DELETE /passengers/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePassenger(@PathVariable Long id) {
        boolean deleted = passengerService.deletePassenger(id);
        
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Passenger not found with ID: " + id));
        }
        
        return ResponseEntity.ok(Map.of("message", "Passenger deleted successfully"));
    }

    /**
     * Search passengers by name
     * GET /passengers/search?keyword=john
     */
    @GetMapping("/search")
    public ResponseEntity<List<Passenger>> searchPassengers(@RequestParam String keyword) {
        List<Passenger> passengers = passengerService.searchPassengersByName(keyword);
        return ResponseEntity.ok(passengers);
    }

    /**
     * Get passengers by country
     * GET /passengers/country/{country}
     */
    @GetMapping("/country/{country}")
    public ResponseEntity<List<Passenger>> getPassengersByCountry(@PathVariable String country) {
        List<Passenger> passengers = passengerService.getPassengersByCountry(country);
        return ResponseEntity.ok(passengers);
    }

    /**
     * Get passengers by nationality
     * GET /passengers/nationality/{nationality}
     */
    @GetMapping("/nationality/{nationality}")
    public ResponseEntity<List<Passenger>> getPassengersByNationality(@PathVariable String nationality) {
        List<Passenger> passengers = passengerService.getPassengersByNationality(nationality);
        return ResponseEntity.ok(passengers);
    }

    // ========================================
    // ENHANCEMENT 1: PROFILE COMPLETENESS METER
    // ========================================

    /**
     * Get profile completeness percentage
     * GET /passengers/{id}/completeness
     * Enhancement: Profile Completeness Meter
     */
    @GetMapping("/{id}/completeness")
    public ResponseEntity<?> getProfileCompleteness(@PathVariable Long id) {
        int completeness = passengerService.getProfileCompleteness(id);
        
        if (completeness == -1) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Passenger not found with ID: " + id));
        }
        
        return ResponseEntity.ok(Map.of(
            "passengerId", id,
            "profileCompleteness", completeness,
            "status", completeness == 100 ? "Complete" : "Incomplete"
        ));
    }

    /**
     * Get missing fields for a passenger
     * GET /passengers/{id}/missing-fields
     * Enhancement: Shows what fields user needs to fill
     */
    @GetMapping("/{id}/missing-fields")
    public ResponseEntity<?> getMissingFields(@PathVariable Long id) {
        List<String> missingFields = passengerService.getMissingFields(id);
        
        if (missingFields.size() == 1 && missingFields.get(0).equals("Passenger not found")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Passenger not found with ID: " + id));
        }
        
        return ResponseEntity.ok(Map.of(
            "passengerId", id,
            "missingFields", missingFields,
            "count", missingFields.size()
        ));
    }

    /**
     * Get incomplete profiles
     * GET /passengers/incomplete
     * Enhancement: Find all passengers with profile < 100%
     */
    @GetMapping("/incomplete")
    public ResponseEntity<List<Passenger>> getIncompleteProfiles() {
        List<Passenger> passengers = passengerService.getIncompleteProfiles();
        return ResponseEntity.ok(passengers);
    }

    /**
     * Get passengers by minimum completeness
     * GET /passengers/completeness/min/{percentage}
     * Example: /passengers/completeness/min/80 (returns passengers with >= 80%)
     */
    @GetMapping("/completeness/min/{percentage}")
    public ResponseEntity<List<Passenger>> getPassengersByCompleteness(@PathVariable int percentage) {
        List<Passenger> passengers = passengerService.getPassengersByCompleteness(percentage);
        return ResponseEntity.ok(passengers);
    }

    // ========================================
    // STATISTICS & UTILITIES
    // ========================================

    /**
     * Get total passenger count
     * GET /passengers/stats/count
     */
    @GetMapping("/stats/count")
    public ResponseEntity<?> getTotalCount() {
        long count = passengerService.getTotalPassengerCount();
        return ResponseEntity.ok(Map.of("totalPassengers", count));
    }

    /**
     * Check if passenger exists
     * GET /passengers/{id}/exists
     */
    @GetMapping("/{id}/exists")
    public ResponseEntity<?> checkPassengerExists(@PathVariable Long id) {
        boolean exists = passengerService.passengerExists(id);
        return ResponseEntity.ok(Map.of(
            "passengerId", id,
            "exists", exists
        ));
    }
}
