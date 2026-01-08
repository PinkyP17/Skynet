package com.skynet.flightcatalog.osgi.api;

import com.skynet.flightcatalog.osgi.model.Flight;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Flight Catalog Service API
 * 
 * This interface defines the contract for the Flight Catalog service.
 * Other bundles can use this service through OSGi Service Registry.
 * 
 * OSGi Principle: Program to interfaces, not implementations
 * 
 * Functionality:
 * 1. Add/Update Flights
 * 2. Search Flights by Date/Route
 * 3. Filter by Price/Duration
 * 4. Update Flight Status (Delayed/On Time)
 * 
 * Enhancements:
 * 1. Flight status color indicator
 * 2. Sort flights by departure time, lowest price, or shortest duration
 * 3. Duplicate flight detection
 */
public interface FlightCatalogService {
    
    // ========================================
    // Functionality 1: Add/Update Flights
    // ========================================
    
    /**
     * Create new flight
     * Enhancement: Includes duplicate detection
     * @param flight flight object
     * @return created flight with ID, or null if duplicate detected
     * @throws IllegalArgumentException if duplicate flight detected
     */
    Flight createFlight(Flight flight) throws IllegalArgumentException;
    
    /**
     * Update existing flight
     * Enhancement: Includes duplicate detection
     * @param id flight ID
     * @param flight updated flight data
     * @return updated flight or null if not found
     */
    Flight updateFlight(Integer id, Flight flight);
    
    /**
     * Delete flight
     * @param id flight ID
     * @return true if deleted, false if not found
     */
    boolean deleteFlight(Integer id);
    
    /**
     * Get flight by ID
     * @param id flight ID
     * @return Flight object or null if not found
     */
    Flight getFlightById(Integer id);
    
    /**
     * Get all flights
     * @return List of all flights
     */
    List<Flight> getAllFlights();
    
    // ========================================
    // Functionality 2: Search Flights by Date/Route
    // ========================================
    
    /**
     * Search flights by departure date
     * @param date departure date
     * @return List of flights on that date
     */
    List<Flight> searchFlightsByDate(LocalDateTime date);
    
    /**
     * Search flights by route (departure and arrival airports)
     * @param depAirportId departure airport ID
     * @param arrAirportId arrival airport ID
     * @return List of flights matching the route
     */
    List<Flight> searchFlightsByRoute(Integer depAirportId, Integer arrAirportId);
    
    /**
     * Search flights by route and date
     * @param depAirportId departure airport ID
     * @param arrAirportId arrival airport ID
     * @param date departure date
     * @return List of flights matching route and date
     */
    List<Flight> searchFlightsByRouteAndDate(Integer depAirportId, Integer arrAirportId, LocalDateTime date);
    
    // ========================================
    // Functionality 3: Filter by Price/Duration
    // ========================================
    
    /**
     * Filter flights by maximum price
     * @param maxPrice maximum price
     * @return List of flights with price <= maxPrice
     */
    List<Flight> filterFlightsByMaxPrice(Double maxPrice);
    
    /**
     * Filter flights by price range
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @return List of flights within price range
     */
    List<Flight> filterFlightsByPriceRange(Double minPrice, Double maxPrice);
    
    /**
     * Filter flights by maximum duration (in minutes)
     * @param maxDurationMinutes maximum duration in minutes
     * @return List of flights with duration <= maxDurationMinutes
     */
    List<Flight> filterFlightsByMaxDuration(Long maxDurationMinutes);
    
    /**
     * Filter flights by duration range (in minutes)
     * @param minDurationMinutes minimum duration in minutes
     * @param maxDurationMinutes maximum duration in minutes
     * @return List of flights within duration range
     */
    List<Flight> filterFlightsByDurationRange(Long minDurationMinutes, Long maxDurationMinutes);
    
    // ========================================
    // Functionality 4: Update Flight Status
    // ========================================
    
    /**
     * Update flight status
     * @param id flight ID
     * @param status new status (ON_TIME, DELAYED, CANCELLED, BOARDING, DEPARTED, ARRIVED)
     * @return updated flight or null if not found
     */
    Flight updateFlightStatus(Integer id, String status);
    
    /**
     * Get flights by status
     * @param status flight status
     * @return List of flights with the specified status
     */
    List<Flight> getFlightsByStatus(String status);
    
    // ========================================
    // Enhancement 2: Sort Flights
    // ========================================
    
    /**
     * Sort flights by departure time
     * @param ascending true for ascending, false for descending
     * @return sorted list of flights
     */
    List<Flight> sortFlightsByDepartureTime(boolean ascending);
    
    /**
     * Sort flights by lowest price
     * @param ascending true for ascending, false for descending
     * @return sorted list of flights
     */
    List<Flight> sortFlightsByPrice(boolean ascending);
    
    /**
     * Sort flights by shortest duration
     * @param ascending true for ascending, false for descending
     * @return sorted list of flights
     */
    List<Flight> sortFlightsByDuration(boolean ascending);
    
    // ========================================
    // Enhancement 3: Duplicate Detection
    // ========================================
    
    /**
     * Check if a flight is duplicate (same route and date)
     * @param flight flight to check
     * @param excludeId flight ID to exclude from check (for updates)
     * @return true if duplicate exists
     */
    boolean isDuplicateFlight(Flight flight, Integer excludeId);
}
