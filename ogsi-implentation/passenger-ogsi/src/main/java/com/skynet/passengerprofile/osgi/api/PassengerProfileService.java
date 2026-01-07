package com.skynet.passengerprofile.osgi.api;

import com.skynet.passengerprofile.osgi.model.Passenger;

import java.util.List;

/**
 * Passenger Profile Service API
 * 
 * This interface defines the contract for the Passenger Profile service.
 * Other bundles can use this service through OSGi Service Registry.
 * 
 * OSGi Principle: Program to interfaces, not implementations
 */
public interface PassengerProfileService {
    
    // ========================================
    // Functionality 1: Manage Personal Details
    // ========================================
    
    /**
     * Get passenger by ID
     * @param id passenger ID
     * @return Passenger object or null if not found
     */
    Passenger getPassenger(Long id);
    
    /**
     * Get all passengers
     * @return List of all passengers
     */
    List<Passenger> getAllPassengers();
    
    /**
     * Create new passenger
     * @param passenger passenger object
     * @return created passenger with ID
     */
    Passenger createPassenger(Passenger passenger);
    
    /**
     * Update passenger details
     * @param id passenger ID
     * @param passenger updated passenger data
     * @return updated passenger or null if not found
     */
    Passenger updatePassenger(Long id, Passenger passenger);
    
    /**
     * Delete passenger
     * @param id passenger ID
     * @return true if deleted, false if not found
     */
    boolean deletePassenger(Long id);
    
    /**
     * Search passengers by name
     * @param keyword search keyword
     * @return List of matching passengers
     */
    List<Passenger> searchPassengersByName(String keyword);
    
    // ========================================
    // Enhancement 1: Profile Completeness Meter
    // ========================================
    
    /**
     * Calculate profile completeness percentage
     * @param id passenger ID
     * @return completeness percentage (0-100)
     */
    int getProfileCompleteness(Long id);
    
    /**
     * Get list of missing fields for a passenger
     * @param id passenger ID
     * @return List of field names that are empty
     */
    List<String> getMissingFields(Long id);
    
    // ========================================
    // Statistics
    // ========================================
    
    /**
     * Get total passenger count
     * @return count of passengers
     */
    long getTotalPassengerCount();
    
    /**
     * Check if passenger exists
     * @param id passenger ID
     * @return true if exists
     */
    boolean passengerExists(Long id);
}
