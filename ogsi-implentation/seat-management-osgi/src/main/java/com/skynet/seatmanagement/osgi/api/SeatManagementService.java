package com.skynet.seatmanagement.osgi.api;

import com.skynet.seatmanagement.osgi.model.Seat;

import java.util.List;

/**
 * Seat Management Service API
 * 
 * This interface defines the contract for the Seat Management service.
 * Other bundles can use this service through OSGi Service Registry.
 * 
 * OSGi Principle: Program to interfaces, not implementations
 */
public interface SeatManagementService {
    
    // ========================================
    // Functionality 1: View Seat Map
    // ========================================
    
    /**
     * Get all seats
     * @return List of all seats
     */
    List<Seat> getAllSeats();
    
    /**
     * Get seat by ID
     * @param id seat ID
     * @return Seat object or null if not found
     */
    Seat getSeatById(Integer id);
    
    /**
     * Get seats by type
     * @param type seat type (First, Business, Economy)
     * @return List of seats of the specified type
     */
    List<Seat> getSeatsByType(String type);
    
    /**
     * Search seat by label (e.g., "A5", "B12")
     * @param label seat label (column + row)
     * @return Seat object or null if not found
     */
    Seat searchSeatByLabel(String label);
    
    // ========================================
    // Functionality 2: Selection/Assign Seat
    // ========================================
    
    /**
     * Check if seat is available for a flight
     * @param seatId seat ID
     * @param flightId flight ID
     * @return true if available, false if reserved
     */
    boolean isSeatAvailable(Integer seatId, Integer flightId);
    
    /**
     * Get available seats for a flight
     * @param flightId flight ID
     * @return List of available seats
     */
    List<Seat> getAvailableSeats(Integer flightId);
    
    /**
     * Get available seats by type for a flight
     * @param flightId flight ID
     * @param type seat type
     * @return List of available seats of the specified type
     */
    List<Seat> getAvailableSeatsByType(Integer flightId, String type);
    
    // ========================================
    // Functionality 3: Release Seat
    // ========================================
    
    /**
     * Release a seat reservation
     * @param seatId seat ID
     * @param flightId flight ID
     * @return true if released successfully
     */
    boolean releaseSeat(Integer seatId, Integer flightId);
    
    // ========================================
    // Functionality 4: Check Seat Availability
    // ========================================
    
    /**
     * Get seat availability statistics for a flight
     * @param flightId flight ID
     * @return Map with statistics (total, occupied, available by type)
     */
    SeatAvailabilityStats getSeatAvailabilityStats(Integer flightId);
    
    /**
     * Get seat availability count for a flight
     * @param flightId flight ID
     * @return number of available seats
     */
    int getAvailableSeatCount(Integer flightId);
    
    // ========================================
    // Enhancement 1: Filter seats by type
    // ========================================
    
    /**
     * Filter seats by type
     * @param type seat type (First, Business, Economy)
     * @return List of filtered seats
     */
    List<Seat> filterSeatsByType(String type);
    
    // ========================================
    // Enhancement 2: Seat search
    // ========================================
    
    /**
     * Search seats by label pattern
     * @param pattern search pattern (e.g., "A", "5", "A5")
     * @return List of matching seats
     */
    List<Seat> searchSeats(String pattern);
    
    // ========================================
    // Statistics
    // ========================================
    
    /**
     * Get total seat count
     * @return count of all seats
     */
    long getTotalSeatCount();
    
    /**
     * Check if seat exists
     * @param id seat ID
     * @return true if exists
     */
    boolean seatExists(Integer id);
    
    /**
     * Seat Availability Statistics
     */
    class SeatAvailabilityStats {
        private int totalSeats;
        private int occupiedSeats;
        private int availableSeats;
        private int availableFirst;
        private int availableBusiness;
        private int availableEconomy;
        
        public SeatAvailabilityStats() {}
        
        public SeatAvailabilityStats(int totalSeats, int occupiedSeats, int availableSeats,
                                    int availableFirst, int availableBusiness, int availableEconomy) {
            this.totalSeats = totalSeats;
            this.occupiedSeats = occupiedSeats;
            this.availableSeats = availableSeats;
            this.availableFirst = availableFirst;
            this.availableBusiness = availableBusiness;
            this.availableEconomy = availableEconomy;
        }
        
        // Getters and setters
        public int getTotalSeats() { return totalSeats; }
        public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }
        
        public int getOccupiedSeats() { return occupiedSeats; }
        public void setOccupiedSeats(int occupiedSeats) { this.occupiedSeats = occupiedSeats; }
        
        public int getAvailableSeats() { return availableSeats; }
        public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
        
        public int getAvailableFirst() { return availableFirst; }
        public void setAvailableFirst(int availableFirst) { this.availableFirst = availableFirst; }
        
        public int getAvailableBusiness() { return availableBusiness; }
        public void setAvailableBusiness(int availableBusiness) { this.availableBusiness = availableBusiness; }
        
        public int getAvailableEconomy() { return availableEconomy; }
        public void setAvailableEconomy(int availableEconomy) { this.availableEconomy = availableEconomy; }
    }
}
