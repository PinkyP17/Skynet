package com.skynet.seatmanagement.controller;

import com.skynet.seatmanagement.model.Seat;
import com.skynet.seatmanagement.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * SeatController - REST API endpoints for Seat Management
 * 
 * Base URL: /api/seat-management/seats
 * 
 * Functionality 1: View Seat Map
 * Functionality 2: Selection/Assign Seat
 * Functionality 3: Release Seat
 * Functionality 4: Check Seat Availability
 * 
 * Enhancement 1: Filter seats by type
 * Enhancement 2: Seat search
 */
@RestController
@RequestMapping("/seats")
@CrossOrigin(origins = "*") // Allow CORS for testing
public class SeatController {

    @Autowired
    private SeatService seatService;

     
    @GetMapping
    public ResponseEntity<List<Seat>> getAllSeats() {
        List<Seat> seats = seatService.getAllSeats();
        return ResponseEntity.ok(seats);
    }

  
    @GetMapping("/{id}")
    public ResponseEntity<?> getSeatById(@PathVariable Integer id) {
        Seat seat = seatService.getSeatById(id);
        
        if (seat == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Seat not found with ID: " + id));
        }
        
        return ResponseEntity.ok(seat);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Seat>> getSeatsByType(@PathVariable String type) {
        List<Seat> seats = seatService.getSeatsByType(type);
        return ResponseEntity.ok(seats);
    }

  
    @GetMapping("/search/label")
    public ResponseEntity<?> searchSeatByLabel(@RequestParam String label) {
        Seat seat = seatService.searchSeatByLabel(label);
        
        if (seat == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Seat not found with label: " + label));
        }
        
        return ResponseEntity.ok(seat); 
    }

    // ========================================
    // FUNCTIONALITY 2: SELECTION/ASSIGN SEAT
    // ========================================

    /**
     * Check if seat is available
     * GET /seats/{seatId}/available?flightId=1
     */
    @GetMapping("/{seatId}/available")
    public ResponseEntity<?> checkSeatAvailability(@PathVariable Integer seatId,
                                                    @RequestParam Integer flightId) {
        boolean available = seatService.isSeatAvailable(seatId, flightId);
        return ResponseEntity.ok(Map.of(
            "seatId", seatId,
            "flightId", flightId,
            "available", available
        ));
    }

    /**
     * Get available seats for a flight
     * GET /seats/available?flightId=1
     */
    @GetMapping("/available")
    public ResponseEntity<List<Seat>> getAvailableSeats(@RequestParam Integer flightId) {
        List<Seat> seats = seatService.getAvailableSeats(flightId);
        return ResponseEntity.ok(seats);
    }

    /**
     * Get available seats by type for a flight
     * GET /seats/available/type?flightId=1&type=First
     */
    @GetMapping("/available/type")
    public ResponseEntity<List<Seat>> getAvailableSeatsByType(@RequestParam Integer flightId,
                                                              @RequestParam String type) {
        List<Seat> seats = seatService.getAvailableSeatsByType(flightId, type);
        return ResponseEntity.ok(seats);
    }

    // ========================================
    // FUNCTIONALITY 3: RELEASE SEAT
    // ========================================

    /**
     * Release a seat
     * DELETE /seats/{seatId}/release?flightId=1
     */
    @DeleteMapping("/{seatId}/release")
    public ResponseEntity<?> releaseSeat(@PathVariable Integer seatId,
                                          @RequestParam Integer flightId) {
        boolean released = seatService.releaseSeat(seatId, flightId);
        
        if (!released) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Failed to release seat"));
        }
        
        return ResponseEntity.ok(Map.of("message", "Seat released successfully"));
    }

    // ========================================
    // FUNCTIONALITY 4: CHECK SEAT AVAILABILITY
    // ========================================

    /**
     * Get seat availability statistics for a flight
     * GET /seats/stats?flightId=1
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getSeatAvailabilityStats(@RequestParam Integer flightId) {
        Map<String, Object> stats = seatService.getSeatAvailabilityStats(flightId);
        return ResponseEntity.ok(stats);
    }

    /**
     * Get available seat count for a flight
     * GET /seats/count/available?flightId=1
     */
    @GetMapping("/count/available")
    public ResponseEntity<?> getAvailableSeatCount(@RequestParam Integer flightId) {
        int count = seatService.getAvailableSeatCount(flightId);
        return ResponseEntity.ok(Map.of(
            "flightId", flightId,
            "availableSeats", count
        ));
    }

    // ========================================
    // ENHANCEMENT 1: FILTER SEATS BY TYPE
    // ========================================

    /**
     * Filter seats by type
     * GET /seats/filter/type?type=First
     */
    @GetMapping("/filter/type")
    public ResponseEntity<List<Seat>> filterSeatsByType(@RequestParam String type) {
        List<Seat> seats = seatService.filterSeatsByType(type);
        return ResponseEntity.ok(seats);
    }

    // ========================================
    // ENHANCEMENT 2: SEAT SEARCH
    // ========================================

    /**
     * Search seats by pattern
     * GET /seats/search?pattern=A5
     */
    @GetMapping("/search")
    public ResponseEntity<List<Seat>> searchSeats(@RequestParam String pattern) {
        List<Seat> seats = seatService.searchSeats(pattern);
        return ResponseEntity.ok(seats);
    }

    /**
     * Get total seat count
     * GET /seats/stats/count
     */
    @GetMapping("/stats/count")
    public ResponseEntity<?> getTotalCount() {
        long count = seatService.getTotalSeatCount();
        return ResponseEntity.ok(Map.of("totalSeats", count));
    }

    /**
     * Get seat count by type
     * GET /seats/stats/count/type?type=First
     */
    @GetMapping("/stats/count/type")
    public ResponseEntity<?> getSeatCountByType(@RequestParam String type) {
        long count = seatService.getSeatCountByType(type);
        return ResponseEntity.ok(Map.of(
            "type", type,
            "count", count
        ));
    }

    /**
     * Check if seat exists
     * GET /seats/{id}/exists
     */
    @GetMapping("/{id}/exists")
    public ResponseEntity<?> checkSeatExists(@PathVariable Integer id) {
        boolean exists = seatService.seatExists(id);
        return ResponseEntity.ok(Map.of(
            "seatId", id,
            "exists", exists
        ));
    }
}
