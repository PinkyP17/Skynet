package com.skynet.seatmanagement.service;

import com.skynet.seatmanagement.model.Seat;
import com.skynet.seatmanagement.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * SeatService - Business logic for Seat Management
 * 
 * Functionality 1: View Seat Map
 * Functionality 2: Selection/Assign Seat
 * Functionality 3: Release Seat
 * Functionality 4: Check Seat Availability
 * 
 * Enhancement 1: Filter seats by type
 * Enhancement 2: Seat search
 */
@Service
@Transactional
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;

    /**
     * Get all seats
     * @return List of all seats
     */
    public List<Seat> getAllSeats() {
        return seatRepository.findAll();
    }

    /**
     * Get seat by ID
     * @param id seat ID
     * @return Seat or null
     */
    public Seat getSeatById(Integer id) {
        return seatRepository.findById(id).orElse(null);
    }

    /**
     * Get seats by type
     * @param type seat type (First, Business, Economy)
     * @return List of seats
     */
    public List<Seat> getSeatsByType(String type) {
        return seatRepository.findByType(type);
    }

    /**
     * Search seat by label (e.g., "A5", "B12")
     * @param label seat label
     * @return Seat or null
     */
    public Seat searchSeatByLabel(String label) {
        if (label == null || label.length() < 2) {
            return null;
        }

        String column = label.substring(0, 1).toUpperCase();
        try {
            Integer row = Integer.parseInt(label.substring(1));
            return seatRepository.findByColumnAndRow(column, row).orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // ========================================
    // FUNCTIONALITY 2: SELECTION/ASSIGN SEAT
    // ========================================

    /**
     * Check if seat is available for a flight
     * Note: This requires integration with ReservationService
     * @param seatId seat ID
     * @param flightId flight ID
     * @return true if available
     */
    public boolean isSeatAvailable(Integer seatId, Integer flightId) {
        // This would need to check reservations table
        // For now, returning true as placeholder
        // In real implementation, would query reservations table
        return true;
    }

    /**
     * Get available seats for a flight
     * @param flightId flight ID
     * @return List of available seats
     */
    public List<Seat> getAvailableSeats(Integer flightId) {
        // This would need integration with ReservationService
        // For now, returning all seats as placeholder
        return getAllSeats();
    }

    /**
     * Get available seats by type for a flight
     * @param flightId flight ID
     * @param type seat type
     * @return List of available seats
     */
    public List<Seat> getAvailableSeatsByType(Integer flightId, String type) {
        return getAvailableSeats(flightId).stream()
                .filter(seat -> seat.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    // ========================================
    // FUNCTIONALITY 3: RELEASE SEAT
    // ========================================

    /**
     * Release a seat reservation
     * Note: This requires integration with ReservationService
     * @param seatId seat ID
     * @param flightId flight ID
     * @return true if released
     */
    public boolean releaseSeat(Integer seatId, Integer flightId) {
        // This would need to delete from reservations table
        // For now, returning true as placeholder
        return true;
    }

    // ========================================
    // FUNCTIONALITY 4: CHECK SEAT AVAILABILITY
    // ========================================

    /**
     * Get seat availability statistics for a flight
     * @param flightId flight ID
     * @return Map with statistics
     */
    public Map<String, Object> getSeatAvailabilityStats(Integer flightId) {
        List<Seat> allSeats = getAllSeats();
        int totalSeats = allSeats.size();
        
        // This would need integration with ReservationService to get actual counts
        // For now, using placeholder values
        int occupiedSeats = 0; // Would query reservations table
        int availableSeats = totalSeats - occupiedSeats;

        // Count seats by type
        long firstCount = allSeats.stream()
                .filter(seat -> seat.getType().equalsIgnoreCase("first"))
                .count();
        long businessCount = allSeats.stream()
                .filter(seat -> seat.getType().equalsIgnoreCase("business"))
                .count();
        long economyCount = allSeats.stream()
                .filter(seat -> seat.getType().equalsIgnoreCase("economy"))
                .count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSeats", totalSeats);
        stats.put("occupiedSeats", occupiedSeats);
        stats.put("availableSeats", availableSeats);
        stats.put("firstClassSeats", firstCount);
        stats.put("businessClassSeats", businessCount);
        stats.put("economyClassSeats", economyCount);
        stats.put("availableFirst", firstCount); // Would calculate from reservations
        stats.put("availableBusiness", businessCount);
        stats.put("availableEconomy", economyCount);

        return stats;
    }

    /**
     * Get available seat count for a flight
     * @param flightId flight ID
     * @return number of available seats
     */
    public int getAvailableSeatCount(Integer flightId) {
        List<Seat> allSeats = getAllSeats();
        // Would subtract occupied seats from reservations
        return allSeats.size();
    }

    // ========================================
    // ENHANCEMENT 1: FILTER SEATS BY TYPE
    // ========================================

    /**
     * Filter seats by type
     * @param type seat type (First, Business, Economy)
     * @return List of filtered seats
     */
    public List<Seat> filterSeatsByType(String type) {
        return seatRepository.findByType(type);
    }

    // ========================================
    // ENHANCEMENT 2: SEAT SEARCH
    // ========================================

    /**
     * Search seats by pattern
     * @param pattern search pattern (e.g., "A", "5", "A5")
     * @return List of matching seats
     */
    public List<Seat> searchSeats(String pattern) {
        if (pattern == null || pattern.trim().isEmpty()) {
            return getAllSeats();
        }
        return seatRepository.searchSeats(pattern.trim());
    }

    // ========================================
    // STATISTICS
    // ========================================

    /**
     * Get total seat count
     * @return count
     */
    public long getTotalSeatCount() {
        return seatRepository.count();
    }

    /**
     * Check if seat exists
     * @param id seat ID
     * @return true if exists
     */
    public boolean seatExists(Integer id) {
        return seatRepository.existsById(id);
    }

    /**
     * Get seat count by type
     * @param type seat type
     * @return count
     */
    public long getSeatCountByType(String type) {
        return seatRepository.countByType(type);
    }
}
