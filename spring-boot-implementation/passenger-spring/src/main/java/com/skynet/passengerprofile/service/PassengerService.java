package com.skynet.passengerprofile.service;

import com.skynet.passengerprofile.model.Passenger;
import com.skynet.passengerprofile.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * PassengerService - Business logic for Passenger Profile management
 * 
 * Functionality 1: Manage Personal Details
 * Enhancement 1: Profile Completeness Meter
 */
@Service
@Transactional
public class PassengerService {

    @Autowired
    private PassengerRepository passengerRepository;

    // ========================================
    // FUNCTIONALITY 1: MANAGE PERSONAL DETAILS
    // ========================================

    /**
     * Get passenger by ID
     * @param id passenger ID
     * @return Passenger or null
     */
    public Passenger getPassengerById(Long id) {
        return passengerRepository.findById(id).orElse(null);
    }

    /**
     * Get all passengers
     * @return List of all passengers
     */
    public List<Passenger> getAllPassengers() {
        return passengerRepository.findAll();
    }

    /**
     * Create new passenger
     * @param passenger passenger object
     * @return saved passenger
     */
    public Passenger createPassenger(Passenger passenger) {
        // Calculate initial profile completeness
        int completeness = calculateProfileCompleteness(passenger);
        passenger.setProfileCompleteness(completeness);
        
        return passengerRepository.save(passenger);
    }

    /**
     * Update passenger details
     * Enhancement: Automatically recalculates profile completeness
     * @param id passenger ID
     * @param updatedPassenger updated passenger data
     * @return updated passenger or null if not found
     */
    public Passenger updatePassenger(Long id, Passenger updatedPassenger) {
        Optional<Passenger> existingOpt = passengerRepository.findById(id);
        
        if (existingOpt.isEmpty()) {
            return null;
        }
        
        Passenger existing = existingOpt.get();
        
        // Update fields
        if (updatedPassenger.getFirstName() != null) {
            existing.setFirstName(updatedPassenger.getFirstName());
        }
        if (updatedPassenger.getLastName() != null) {
            existing.setLastName(updatedPassenger.getLastName());
        }
        if (updatedPassenger.getBirthDate() != null) {
            existing.setBirthDate(updatedPassenger.getBirthDate());
        }
        if (updatedPassenger.getGender() != null) {
            existing.setGender(updatedPassenger.getGender());
        }
        if (updatedPassenger.getCountry() != null) {
            existing.setCountry(updatedPassenger.getCountry());
        }
        if (updatedPassenger.getPhoneNumber() != null) {
            existing.setPhoneNumber(updatedPassenger.getPhoneNumber());
        }
        if (updatedPassenger.getAddress() != null) {
            existing.setAddress(updatedPassenger.getAddress());
        }
        if (updatedPassenger.getNationality() != null) {
            existing.setNationality(updatedPassenger.getNationality());
        }
        if (updatedPassenger.getMealPreference() != null) {
            existing.setMealPreference(updatedPassenger.getMealPreference());
        }
        if (updatedPassenger.getProfilePicture() != null) {
            existing.setProfilePicture(updatedPassenger.getProfilePicture());
        }
        
        // Recalculate profile completeness after update
        int completeness = calculateProfileCompleteness(existing);
        existing.setProfileCompleteness(completeness);
        
        return passengerRepository.save(existing);
    }

    /**
     * Delete passenger
     * @param id passenger ID
     * @return true if deleted, false if not found
     */
    public boolean deletePassenger(Long id) {
        if (passengerRepository.existsById(id)) {
            passengerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Search passengers by name
     * @param keyword search keyword
     * @return List of matching passengers
     */
    public List<Passenger> searchPassengersByName(String keyword) {
        return passengerRepository.searchByName(keyword);
    }

    /**
     * Get passengers by country
     * @param country country name
     * @return List of passengers
     */
    public List<Passenger> getPassengersByCountry(String country) {
        return passengerRepository.findByCountry(country);
    }

    /**
     * Get passengers by nationality
     * @param nationality nationality
     * @return List of passengers
     */
    public List<Passenger> getPassengersByNationality(String nationality) {
        return passengerRepository.findByNationality(nationality);
    }

    // ========================================
    // ENHANCEMENT 1: PROFILE COMPLETENESS METER
    // ========================================

    /**
     * Calculate profile completeness percentage
     * Enhancement: Profile Completeness Meter
     * @param passenger passenger object
     * @return completeness percentage (0-100)
     */
    public int calculateProfileCompleteness(Passenger passenger) {
        int totalFields = 11; // Total editable fields
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
        
        // Bonus for having documents
        if (passenger.getTravelDocuments() != null && !passenger.getTravelDocuments().isEmpty()) {
            filledFields++;
        }

        return (int) ((filledFields / (double) totalFields) * 100);
    }

    /**
     * Get profile completeness for a passenger
     * @param id passenger ID
     * @return completeness percentage or -1 if not found
     */
    public int getProfileCompleteness(Long id) {
        Optional<Passenger> passengerOpt = passengerRepository.findById(id);
        
        if (passengerOpt.isEmpty()) {
            return -1;
        }
        
        Passenger passenger = passengerOpt.get();
        return calculateProfileCompleteness(passenger);
    }

    /**
     * Get passengers with incomplete profiles
     * @return List of passengers with profile < 100%
     */
    public List<Passenger> getIncompleteProfiles() {
        return passengerRepository.findIncompleteProfiles();
    }

    /**
     * Get passengers by minimum profile completeness
     * @param minPercentage minimum completeness percentage
     * @return List of passengers
     */
    public List<Passenger> getPassengersByCompleteness(int minPercentage) {
        return passengerRepository.findByProfileCompletenessGreaterThanEqual(minPercentage);
    }

    /**
     * Get missing fields for a passenger
     * Enhancement: Help users know what to fill
     * @param id passenger ID
     * @return List of missing field names
     */
    public List<String> getMissingFields(Long id) {
        Optional<Passenger> passengerOpt = passengerRepository.findById(id);
        
        if (passengerOpt.isEmpty()) {
            return List.of("Passenger not found");
        }
        
        Passenger p = passengerOpt.get();
        List<String> missing = new java.util.ArrayList<>();
        
        if (isEmpty(p.getFirstName())) missing.add("First Name");
        if (isEmpty(p.getLastName())) missing.add("Last Name");
        if (isEmpty(p.getBirthDate())) missing.add("Birth Date");
        if (isEmpty(p.getGender())) missing.add("Gender");
        if (isEmpty(p.getCountry())) missing.add("Country");
        if (isEmpty(p.getPhoneNumber())) missing.add("Phone Number");
        if (isEmpty(p.getAddress())) missing.add("Address");
        if (isEmpty(p.getNationality())) missing.add("Nationality");
        if (isEmpty(p.getMealPreference())) missing.add("Meal Preference");
        if (isEmpty(p.getProfilePicture())) missing.add("Profile Picture");
        if (p.getTravelDocuments() == null || p.getTravelDocuments().isEmpty()) {
            missing.add("Travel Documents");
        }
        
        return missing;
    }

    // ========================================
    // HELPER METHODS
    // ========================================

    private boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Get total passenger count
     * @return count
     */
    public long getTotalPassengerCount() {
        return passengerRepository.count();
    }

    /**
     * Check if passenger exists
     * @param id passenger ID
     * @return true if exists
     */
    public boolean passengerExists(Long id) {
        return passengerRepository.existsById(id);
    }
}
