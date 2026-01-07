package com.skynet.passengerprofile.osgi.impl;

import com.skynet.passengerprofile.osgi.api.PassengerProfileService;
import com.skynet.passengerprofile.osgi.dao.PassengerDao;
import com.skynet.passengerprofile.osgi.model.Passenger;

import java.util.ArrayList;
import java.util.List;

/**
 * Passenger Profile Service Implementation
 * 
 * This is the actual implementation of the PassengerProfileService interface.
 * It contains the business logic for passenger profile management.
 * 
 * This class will be registered in the OSGi Service Registry by the Activator.
 */
public class PassengerProfileServiceImpl implements PassengerProfileService {
    
    private final PassengerDao passengerDao;
    
    public PassengerProfileServiceImpl() {
        this.passengerDao = new PassengerDao();
        System.out.println("[PassengerProfileService] Service implementation created");
    }
    
    // ========================================
    // Functionality 1: Manage Personal Details
    // ========================================
    
    @Override
    public Passenger getPassenger(Long id) {
        System.out.println("[PassengerProfileService] Getting passenger: " + id);
        return passengerDao.findById(id);
    }
    
    @Override
    public List<Passenger> getAllPassengers() {
        System.out.println("[PassengerProfileService] Getting all passengers");
        return passengerDao.findAll();
    }
    
    @Override
    public Passenger createPassenger(Passenger passenger) {
        System.out.println("[PassengerProfileService] Creating passenger: " + passenger.getFullName());
        
        // Calculate initial completeness
        int completeness = calculateCompleteness(passenger);
        passenger.setProfileCompleteness(completeness);
        
        return passengerDao.save(passenger);
    }
    
    @Override
    public Passenger updatePassenger(Long id, Passenger passenger) {
        System.out.println("[PassengerProfileService] Updating passenger: " + id);
        
        // Get existing passenger
        Passenger existing = passengerDao.findById(id);
        if (existing == null) {
            System.err.println("[PassengerProfileService] Passenger not found: " + id);
            return null;
        }
        
        // Update fields (only non-null values)
        if (passenger.getFirstName() != null) existing.setFirstName(passenger.getFirstName());
        if (passenger.getLastName() != null) existing.setLastName(passenger.getLastName());
        if (passenger.getBirthDate() != null) existing.setBirthDate(passenger.getBirthDate());
        if (passenger.getGender() != null) existing.setGender(passenger.getGender());
        if (passenger.getCountry() != null) existing.setCountry(passenger.getCountry());
        if (passenger.getPhoneNumber() != null) existing.setPhoneNumber(passenger.getPhoneNumber());
        if (passenger.getAddress() != null) existing.setAddress(passenger.getAddress());
        if (passenger.getNationality() != null) existing.setNationality(passenger.getNationality());
        if (passenger.getMealPreference() != null) existing.setMealPreference(passenger.getMealPreference());
        if (passenger.getProfilePicture() != null) existing.setProfilePicture(passenger.getProfilePicture());
        
        // Recalculate completeness
        int completeness = calculateCompleteness(existing);
        existing.setProfileCompleteness(completeness);
        
        return passengerDao.update(id, existing);
    }
    
    @Override
    public boolean deletePassenger(Long id) {
        System.out.println("[PassengerProfileService] Deleting passenger: " + id);
        return passengerDao.delete(id);
    }
    
    @Override
    public List<Passenger> searchPassengersByName(String keyword) {
        System.out.println("[PassengerProfileService] Searching passengers: " + keyword);
        return passengerDao.searchByName(keyword);
    }
    
    // ========================================
    // Enhancement 1: Profile Completeness Meter
    // ========================================
    
    @Override
    public int getProfileCompleteness(Long id) {
        System.out.println("[PassengerProfileService] Getting profile completeness: " + id);
        Passenger passenger = passengerDao.findById(id);
        if (passenger == null) {
            return -1;
        }
        return calculateCompleteness(passenger);
    }
    
    @Override
    public List<String> getMissingFields(Long id) {
        System.out.println("[PassengerProfileService] Getting missing fields: " + id);
        Passenger passenger = passengerDao.findById(id);
        if (passenger == null) {
            return List.of("Passenger not found");
        }
        
        List<String> missing = new ArrayList<>();
        
        if (isEmpty(passenger.getFirstName())) missing.add("First Name");
        if (isEmpty(passenger.getLastName())) missing.add("Last Name");
        if (isEmpty(passenger.getBirthDate())) missing.add("Birth Date");
        if (isEmpty(passenger.getGender())) missing.add("Gender");
        if (isEmpty(passenger.getCountry())) missing.add("Country");
        if (isEmpty(passenger.getPhoneNumber())) missing.add("Phone Number");
        if (isEmpty(passenger.getAddress())) missing.add("Address");
        if (isEmpty(passenger.getNationality())) missing.add("Nationality");
        if (isEmpty(passenger.getMealPreference())) missing.add("Meal Preference");
        if (isEmpty(passenger.getProfilePicture())) missing.add("Profile Picture");
        
        return missing;
    }
    
    /**
     * Calculate profile completeness percentage
     * Enhancement: Profile Completeness Meter
     */
    private int calculateCompleteness(Passenger passenger) {
        int totalFields = 10; // Total editable fields
        int filledFields = 0;
        
        if (isNotEmpty(passenger.getFirstName())) filledFields++;
        if (isNotEmpty(passenger.getLastName())) filledFields++;
        if (isNotEmpty(passenger.getBirthDate())) filledFields++;
        if (isNotEmpty(passenger.getGender())) filledFields++;
        if (isNotEmpty(passenger.getCountry())) filledFields++;
        if (isNotEmpty(passenger.getPhoneNumber())) filledFields++;
        if (isNotEmpty(passenger.getAddress())) filledFields++;
        if (isNotEmpty(passenger.getNationality())) filledFields++;
        if (isNotEmpty(passenger.getMealPreference())) filledFields++;
        if (isNotEmpty(passenger.getProfilePicture())) filledFields++;
        
        return (int) ((filledFields / (double) totalFields) * 100);
    }
    
    // ========================================
    // Statistics
    // ========================================
    
    @Override
    public long getTotalPassengerCount() {
        return passengerDao.count();
    }
    
    @Override
    public boolean passengerExists(Long id) {
        return passengerDao.exists(id);
    }
    
    // ========================================
    // Helper Methods
    // ========================================
    
    private boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
    
    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
