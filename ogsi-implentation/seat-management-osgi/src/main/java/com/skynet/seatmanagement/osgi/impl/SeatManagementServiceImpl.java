package com.skynet.seatmanagement.osgi.impl;

import com.skynet.seatmanagement.osgi.api.SeatManagementService;
import com.skynet.seatmanagement.osgi.dao.SeatDao;
import com.skynet.seatmanagement.osgi.model.Seat;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Seat Management Service Implementation
 * 
 * This is the actual implementation of the SeatManagementService interface.
 * It contains the business logic for seat management.
 * 
 * This class will be registered in the OSGi Service Registry by the Activator.
 */
public class SeatManagementServiceImpl implements SeatManagementService {
    
    private final SeatDao seatDao;
    
    public SeatManagementServiceImpl() {
        this.seatDao = new SeatDao();
        System.out.println("[SeatManagementService] Service implementation created");
    }
    
    // ========================================
    // Functionality 1: View Seat Map
    // ========================================
    
    @Override
    public List<Seat> getAllSeats() {
        System.out.println("[SeatManagementService] Getting all seats");
        return seatDao.findAll();
    }
    
    @Override
    public Seat getSeatById(Integer id) {
        System.out.println("[SeatManagementService] Getting seat: " + id);
        return seatDao.findById(id);
    }
    
    @Override
    public List<Seat> getSeatsByType(String type) {
        System.out.println("[SeatManagementService] Getting seats by type: " + type);
        return seatDao.findByType(type);
    }
    
    @Override
    public Seat searchSeatByLabel(String label) {
        System.out.println("[SeatManagementService] Searching seat by label: " + label);
        
        if (label == null || label.length() < 2) {
            return null;
        }
        
        // Parse label (e.g., "A5" -> column="A", row=5)
        String column = label.substring(0, 1).toUpperCase();
        try {
            Integer row = Integer.parseInt(label.substring(1));
            return seatDao.findByLabel(column, row);
        } catch (NumberFormatException e) {
            System.err.println("Invalid seat label format: " + label);
            return null;
        }
    }
    
    // ========================================
    // Functionality 2: Selection/Assign Seat
    // ========================================
    
    @Override
    public boolean isSeatAvailable(Integer seatId, Integer flightId) {
        System.out.println("[SeatManagementService] Checking seat availability: seatId=" + seatId + ", flightId=" + flightId);
        return !seatDao.isSeatReserved(seatId, flightId);
    }
    
    @Override
    public List<Seat> getAvailableSeats(Integer flightId) {
        System.out.println("[SeatManagementService] Getting available seats for flight: " + flightId);
        return seatDao.findAll().stream()
                .filter(seat -> isSeatAvailable(seat.getId(), flightId))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Seat> getAvailableSeatsByType(Integer flightId, String type) {
        System.out.println("[SeatManagementService] Getting available seats by type: flightId=" + flightId + ", type=" + type);
        return getAvailableSeats(flightId).stream()
                .filter(seat -> seat.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }
    
    // ========================================
    // Functionality 3: Release Seat
    // ========================================
    
    @Override
    public boolean releaseSeat(Integer seatId, Integer flightId) {
        System.out.println("[SeatManagementService] Releasing seat: seatId=" + seatId + ", flightId=" + flightId);
        // Note: Actual release would require ReservationDao
        // For now, this is a placeholder that would need integration with reservation system
        return true;
    }
    
    // ========================================
    // Functionality 4: Check Seat Availability
    // ========================================
    
    @Override
    public SeatAvailabilityStats getSeatAvailabilityStats(Integer flightId) {
        System.out.println("[SeatManagementService] Getting seat availability stats for flight: " + flightId);
        
        List<Seat> allSeats = seatDao.findAll();
        int totalSeats = allSeats.size();
        int occupiedSeats = seatDao.countReservationsForFlight(flightId);
        int availableSeats = totalSeats - occupiedSeats;
        
        // Count available seats by type
        List<Seat> availableSeatsList = getAvailableSeats(flightId);
        int availableFirst = (int) availableSeatsList.stream()
                .filter(seat -> seat.getType().equalsIgnoreCase("first"))
                .count();
        int availableBusiness = (int) availableSeatsList.stream()
                .filter(seat -> seat.getType().equalsIgnoreCase("business"))
                .count();
        int availableEconomy = (int) availableSeatsList.stream()
                .filter(seat -> seat.getType().equalsIgnoreCase("economy"))
                .count();
        
        return new SeatAvailabilityStats(
                totalSeats,
                occupiedSeats,
                availableSeats,
                availableFirst,
                availableBusiness,
                availableEconomy
        );
    }
    
    @Override
    public int getAvailableSeatCount(Integer flightId) {
        System.out.println("[SeatManagementService] Getting available seat count for flight: " + flightId);
        List<Seat> allSeats = seatDao.findAll();
        int occupiedSeats = seatDao.countReservationsForFlight(flightId);
        return allSeats.size() - occupiedSeats;
    }
    
    // ========================================
    // Enhancement 1: Filter seats by type
    // ========================================
    
    @Override
    public List<Seat> filterSeatsByType(String type) {
        System.out.println("[SeatManagementService] Filtering seats by type: " + type);
        return seatDao.findByType(type);
    }
    
    // ========================================
    // Enhancement 2: Seat search
    // ========================================
    
    @Override
    public List<Seat> searchSeats(String pattern) {
        System.out.println("[SeatManagementService] Searching seats with pattern: " + pattern);
        
        if (pattern == null || pattern.trim().isEmpty()) {
            return getAllSeats();
        }
        
        String upperPattern = pattern.toUpperCase().trim();
        return seatDao.findAll().stream()
                .filter(seat -> {
                    String label = seat.getLabel().toUpperCase();
                    String column = seat.getColumn().toUpperCase();
                    String row = seat.getRow().toString();
                    String type = seat.getType().toUpperCase();
                    
                    return label.contains(upperPattern) ||
                           column.contains(upperPattern) ||
                           row.contains(upperPattern) ||
                           type.contains(upperPattern);
                })
                .collect(Collectors.toList());
    }
    
    // ========================================
    // Statistics
    // ========================================
    
    @Override
    public long getTotalSeatCount() {
        return seatDao.count();
    }
    
    @Override
    public boolean seatExists(Integer id) {
        return seatDao.exists(id);
    }
}
