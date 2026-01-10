package com.skynet.flightcatalog.service;

import com.skynet.flightcatalog.model.Flight;
import com.skynet.flightcatalog.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FlightCatalogService {
    
    @Autowired
    private FlightRepository flightRepository;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    // ========================================
    // Functionality 1: Add/Update Flights
    // ========================================
    
    public Flight addFlight(Flight flight) {
        // Duplicate detection
        String dateStr = flight.getDepDatetime().format(DATE_FORMATTER);
        if (flightRepository.existsDuplicate(
                flight.getDepAirportId(),
                flight.getArrAirportId(),
                dateStr,
                flight.getAirlineId())) {
            throw new IllegalArgumentException("Duplicate flight detected: A flight with the same route, date, and airline already exists.");
        }
        
        // Set default status if not provided
        if (flight.getStatus() == null || flight.getStatus().trim().isEmpty()) {
            flight.setStatus("On Time");
        }
        
        return flightRepository.save(flight);
    }
    
    public Flight updateFlight(Integer id, Flight flight) {
        Flight existingFlight = flightRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Flight not found with id: " + id));
        
        // Duplicate detection (excluding current flight)
        String dateStr = flight.getDepDatetime().format(DATE_FORMATTER);
        if (flightRepository.existsDuplicateExcluding(
                flight.getDepAirportId(),
                flight.getArrAirportId(),
                dateStr,
                flight.getAirlineId(),
                id)) {
            throw new IllegalArgumentException("Duplicate flight detected: Another flight with the same route, date, and airline already exists.");
        }
        
        // Update fields
        existingFlight.setAirlineId(flight.getAirlineId());
        existingFlight.setDepAirportId(flight.getDepAirportId());
        existingFlight.setArrAirportId(flight.getArrAirportId());
        existingFlight.setDepDatetime(flight.getDepDatetime());
        existingFlight.setArrDatetime(flight.getArrDatetime());
        existingFlight.setFirstPrice(flight.getFirstPrice());
        existingFlight.setBusinessPrice(flight.getBusinessPrice());
        existingFlight.setEconomyPrice(flight.getEconomyPrice());
        existingFlight.setLuggagePrice(flight.getLuggagePrice());
        existingFlight.setWeightPrice(flight.getWeightPrice());
        // Status is updated separately via updateStatus method
        
        return flightRepository.save(existingFlight);
    }
    
    public void deleteFlight(Integer id) {
        if (!flightRepository.existsById(id)) {
            throw new IllegalArgumentException("Flight not found with id: " + id);
        }
        flightRepository.deleteById(id);
    }
    
    public Flight getFlightById(Integer id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Flight not found with id: " + id));
    }
    
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }
    
    public List<Flight> getFlightsByAirline(Integer airlineId) {
        return flightRepository.findByAirlineId(airlineId);
    }
    
    // ========================================
    // Functionality 2: Search Flights by Date/Route
    // ========================================
    
    public List<Flight> searchFlightsByDate(LocalDateTime date) {
        String dateStr = date.format(DATE_FORMATTER);
        return flightRepository.findByDepDatetimeDate(dateStr);
    }
    
    public List<Flight> searchFlightsByRoute(Integer depAirportId, Integer arrAirportId) {
        return flightRepository.findByDepAirportIdAndArrAirportId(depAirportId, arrAirportId);
    }
    
    public List<Flight> searchFlightsByRouteAndDate(Integer depAirportId, Integer arrAirportId, LocalDateTime date) {
        String dateStr = date.format(DATE_FORMATTER);
        return flightRepository.findByRouteAndDate(depAirportId, arrAirportId, dateStr);
    }
    
    // ========================================
    // Functionality 3: Filter by Price/Duration
    // ========================================
    
    public List<Flight> filterFlightsByMaxPrice(Double maxPrice) {
        List<Flight> allFlights = flightRepository.findAll();
        return allFlights.stream()
                .filter(f -> f.getMinPrice() != null && f.getMinPrice() <= maxPrice)
                .collect(Collectors.toList());
    }
    
    public List<Flight> filterFlightsByPriceRange(Double minPrice, Double maxPrice) {
        List<Flight> allFlights = flightRepository.findAll();
        return allFlights.stream()
                .filter(f -> {
                    Double price = f.getMinPrice();
                    return price != null && price >= minPrice && price <= maxPrice;
                })
                .collect(Collectors.toList());
    }
    
    public List<Flight> filterFlightsByMaxDuration(Long maxDurationMinutes) {
        List<Flight> allFlights = flightRepository.findAll();
        return allFlights.stream()
                .filter(f -> {
                    Long duration = f.getDurationMinutes();
                    return duration != null && duration <= maxDurationMinutes;
                })
                .collect(Collectors.toList());
    }
    
    public List<Flight> filterFlightsByDurationRange(Long minDurationMinutes, Long maxDurationMinutes) {
        List<Flight> allFlights = flightRepository.findAll();
        return allFlights.stream()
                .filter(f -> {
                    Long duration = f.getDurationMinutes();
                    return duration != null && duration >= minDurationMinutes && duration <= maxDurationMinutes;
                })
                .collect(Collectors.toList());
    }
    
    // ========================================
    // Functionality 4: Update Flight Status
    // ========================================
    
    public Flight updateFlightStatus(Integer id, String status) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Flight not found with id: " + id));
        
        if (status == null || status.trim().isEmpty()) {
            status = "On Time";
        }
        
        flight.setStatus(status);
        return flightRepository.save(flight);
    }
    
    public List<Flight> getFlightsByStatus(String status) {
        return flightRepository.findByStatus(status);
    }
    
    // ========================================
    // Enhancement 2: Sort Flights
    // ========================================
    
    public List<Flight> sortFlightsByDepartureTime(List<Flight> flights) {
        return flights.stream()
                .sorted(Comparator.comparing(Flight::getDepDatetime))
                .collect(Collectors.toList());
    }
    
    public List<Flight> sortFlightsByLowestPrice(List<Flight> flights) {
        return flights.stream()
                .sorted(Comparator.comparing(f -> f.getMinPrice() != null ? f.getMinPrice() : Double.MAX_VALUE))
                .collect(Collectors.toList());
    }
    
    public List<Flight> sortFlightsByShortestDuration(List<Flight> flights) {
        return flights.stream()
                .sorted(Comparator.comparing(f -> f.getDurationMinutes() != null ? f.getDurationMinutes() : Long.MAX_VALUE))
                .collect(Collectors.toList());
    }
    
    // ========================================
    // Enhancement 3: Duplicate Detection
    // ========================================
    
    public boolean isDuplicateFlight(Integer depAirportId, Integer arrAirportId, LocalDateTime date, Integer airlineId) {
        String dateStr = date.format(DATE_FORMATTER);
        return flightRepository.existsDuplicate(depAirportId, arrAirportId, dateStr, airlineId);
    }
    
    public boolean isDuplicateFlightExcluding(Integer depAirportId, Integer arrAirportId, LocalDateTime date, Integer airlineId, Integer excludeId) {
        String dateStr = date.format(DATE_FORMATTER);
        return flightRepository.existsDuplicateExcluding(depAirportId, arrAirportId, dateStr, airlineId, excludeId);
    }
}
