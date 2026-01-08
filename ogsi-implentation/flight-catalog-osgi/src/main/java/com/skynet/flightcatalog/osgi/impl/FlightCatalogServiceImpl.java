package com.skynet.flightcatalog.osgi.impl;

import com.skynet.flightcatalog.osgi.api.FlightCatalogService;
import com.skynet.flightcatalog.osgi.dao.FlightDao;
import com.skynet.flightcatalog.osgi.model.Flight;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Flight Catalog Service Implementation
 * 
 * This is the actual implementation of the FlightCatalogService interface.
 * It contains the business logic for flight catalog management.
 * 
 * This class will be registered in the OSGi Service Registry by the Activator.
 */
public class FlightCatalogServiceImpl implements FlightCatalogService {
    
    private final FlightDao flightDao;
    
    public FlightCatalogServiceImpl() {
        this.flightDao = new FlightDao();
        System.out.println("[FlightCatalogService] Service implementation created");
    }
    
    // ========================================
    // Functionality 1: Add/Update Flights
    // ========================================
    
    @Override
    public Flight createFlight(Flight flight) throws IllegalArgumentException {
        System.out.println("[FlightCatalogService] Creating flight: " + flight.getDepAirportId() + " -> " + flight.getArrAirportId());
        
        // Enhancement: Duplicate detection
        if (isDuplicateFlight(flight, null)) {
            String errorMsg = "Duplicate flight detected: Flight with same route (" +
                    flight.getDepAirportId() + " -> " + flight.getArrAirportId() +
                    ") and departure time (" + flight.getDepDatetime() + ") already exists";
            throw new IllegalArgumentException(errorMsg);
        }
        
        // Set default status if not provided
        if (flight.getStatus() == null || flight.getStatus().isBlank()) {
            flight.setStatus("ON_TIME");
        }
        
        return flightDao.save(flight);
    }
    
    @Override
    public Flight updateFlight(Integer id, Flight flight) {
        System.out.println("[FlightCatalogService] Updating flight: " + id);
        
        Flight existing = flightDao.findById(id);
        if (existing == null) {
            return null;
        }
        
        // Enhancement: Duplicate detection (exclude current flight)
        if (isDuplicateFlight(flight, id)) {
            System.out.println("[FlightCatalogService] Warning: Duplicate flight detected, but continuing update");
        }
        
        // Preserve status if not provided
        if (flight.getStatus() == null || flight.getStatus().isBlank()) {
            flight.setStatus(existing.getStatus());
        }
        
        flight.setId(id);
        return flightDao.update(flight);
    }
    
    @Override
    public boolean deleteFlight(Integer id) {
        System.out.println("[FlightCatalogService] Deleting flight: " + id);
        return flightDao.delete(id);
    }
    
    @Override
    public Flight getFlightById(Integer id) {
        System.out.println("[FlightCatalogService] Getting flight: " + id);
        return flightDao.findById(id);
    }
    
    @Override
    public List<Flight> getAllFlights() {
        System.out.println("[FlightCatalogService] Getting all flights");
        return flightDao.findAll();
    }
    
    // ========================================
    // Functionality 2: Search Flights by Date/Route
    // ========================================
    
    @Override
    public List<Flight> searchFlightsByDate(LocalDateTime date) {
        System.out.println("[FlightCatalogService] Searching flights by date: " + date);
        return flightDao.findByDate(date);
    }
    
    @Override
    public List<Flight> searchFlightsByRoute(Integer depAirportId, Integer arrAirportId) {
        System.out.println("[FlightCatalogService] Searching flights by route: " + depAirportId + " -> " + arrAirportId);
        return flightDao.findByRoute(depAirportId, arrAirportId);
    }
    
    @Override
    public List<Flight> searchFlightsByRouteAndDate(Integer depAirportId, Integer arrAirportId, LocalDateTime date) {
        System.out.println("[FlightCatalogService] Searching flights by route and date: " + depAirportId + " -> " + arrAirportId + " on " + date);
        return flightDao.findByRouteAndDate(depAirportId, arrAirportId, date);
    }
    
    // ========================================
    // Functionality 3: Filter by Price/Duration
    // ========================================
    
    @Override
    public List<Flight> filterFlightsByMaxPrice(Double maxPrice) {
        System.out.println("[FlightCatalogService] Filtering flights by max price: " + maxPrice);
        List<Flight> allFlights = flightDao.findAll();
        return allFlights.stream()
                .filter(f -> f.getMinPrice() <= maxPrice)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Flight> filterFlightsByPriceRange(Double minPrice, Double maxPrice) {
        System.out.println("[FlightCatalogService] Filtering flights by price range: " + minPrice + " - " + maxPrice);
        List<Flight> allFlights = flightDao.findAll();
        return allFlights.stream()
                .filter(f -> {
                    double price = f.getMinPrice();
                    return price >= minPrice && price <= maxPrice;
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Flight> filterFlightsByMaxDuration(Long maxDurationMinutes) {
        System.out.println("[FlightCatalogService] Filtering flights by max duration: " + maxDurationMinutes + " minutes");
        List<Flight> allFlights = flightDao.findAll();
        return allFlights.stream()
                .filter(f -> f.getDurationMinutes() <= maxDurationMinutes)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Flight> filterFlightsByDurationRange(Long minDurationMinutes, Long maxDurationMinutes) {
        System.out.println("[FlightCatalogService] Filtering flights by duration range: " + minDurationMinutes + " - " + maxDurationMinutes + " minutes");
        List<Flight> allFlights = flightDao.findAll();
        return allFlights.stream()
                .filter(f -> {
                    long duration = f.getDurationMinutes();
                    return duration >= minDurationMinutes && duration <= maxDurationMinutes;
                })
                .collect(Collectors.toList());
    }
    
    // ========================================
    // Functionality 4: Update Flight Status
    // ========================================
    
    @Override
    public Flight updateFlightStatus(Integer id, String status) {
        System.out.println("[FlightCatalogService] Updating flight status: " + id + " -> " + status);
        
        Flight flight = flightDao.findById(id);
        if (flight == null) {
            return null;
        }
        
        if (flightDao.updateStatus(id, status)) {
            flight.setStatus(status);
            return flight;
        }
        
        return null;
    }
    
    @Override
    public List<Flight> getFlightsByStatus(String status) {
        System.out.println("[FlightCatalogService] Getting flights by status: " + status);
        return flightDao.findByStatus(status);
    }
    
    // ========================================
    // Enhancement 2: Sort Flights
    // ========================================
    
    @Override
    public List<Flight> sortFlightsByDepartureTime(boolean ascending) {
        System.out.println("[FlightCatalogService] Sorting flights by departure time: " + (ascending ? "ascending" : "descending"));
        List<Flight> allFlights = new ArrayList<>(flightDao.findAll());
        
        allFlights.sort((f1, f2) -> {
            int comparison = f1.getDepDatetime().compareTo(f2.getDepDatetime());
            return ascending ? comparison : -comparison;
        });
        
        return allFlights;
    }
    
    @Override
    public List<Flight> sortFlightsByPrice(boolean ascending) {
        System.out.println("[FlightCatalogService] Sorting flights by price: " + (ascending ? "ascending" : "descending"));
        List<Flight> allFlights = new ArrayList<>(flightDao.findAll());
        
        allFlights.sort((f1, f2) -> {
            double price1 = f1.getMinPrice();
            double price2 = f2.getMinPrice();
            int comparison = Double.compare(price1, price2);
            return ascending ? comparison : -comparison;
        });
        
        return allFlights;
    }
    
    @Override
    public List<Flight> sortFlightsByDuration(boolean ascending) {
        System.out.println("[FlightCatalogService] Sorting flights by duration: " + (ascending ? "ascending" : "descending"));
        List<Flight> allFlights = new ArrayList<>(flightDao.findAll());
        
        allFlights.sort((f1, f2) -> {
            long dur1 = f1.getDurationMinutes();
            long dur2 = f2.getDurationMinutes();
            int comparison = Long.compare(dur1, dur2);
            return ascending ? comparison : -comparison;
        });
        
        return allFlights;
    }
    
    // ========================================
    // Enhancement 3: Duplicate Detection
    // ========================================
    
    @Override
    public boolean isDuplicateFlight(Flight flight, Integer excludeId) {
        return flightDao.isDuplicate(flight, excludeId);
    }
}
