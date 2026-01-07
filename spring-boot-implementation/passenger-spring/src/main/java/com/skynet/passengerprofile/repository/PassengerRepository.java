package com.skynet.passengerprofile.repository;

import com.skynet.passengerprofile.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * PassengerRepository - Data access layer for Passenger entity
 * 
 * Spring Data JPA automatically provides:
 * - save(Passenger) - Create or update
 * - findById(Long) - Read by ID
 * - findAll() - Read all
 * - deleteById(Long) - Delete by ID
 * - count() - Count total records
 * 
 * Custom methods defined below for specific queries
 */
@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    /**
     * Find passenger by first name and last name
     * @param firstName first name
     * @param lastName last name
     * @return Optional<Passenger>
     */
    Optional<Passenger> findByFirstNameAndLastName(String firstName, String lastName);

    /**
     * Find passengers by country
     * @param country country name
     * @return List of passengers
     */
    List<Passenger> findByCountry(String country);

    /**
     * Find passengers by nationality
     * @param nationality nationality
     * @return List of passengers
     */
    List<Passenger> findByNationality(String nationality);

    /**
     * Find passengers by gender
     * @param gender "M" or "F"
     * @return List of passengers
     */
    List<Passenger> findByGender(String gender);

    /**
     * Find passengers with profile completeness greater than or equal to percentage
     * Enhancement: Profile Completeness Meter
     * @param percentage minimum completeness percentage
     * @return List of passengers
     */
    List<Passenger> findByProfileCompletenessGreaterThanEqual(Integer percentage);

    /**
     * Find passengers with incomplete profiles (less than 100%)
     * Enhancement: Profile Completeness Meter
     * @return List of passengers with incomplete profiles
     */
    @Query("SELECT p FROM Passenger p WHERE p.profileCompleteness < 100 OR p.profileCompleteness IS NULL")
    List<Passenger> findIncompleteProfiles();

    /**
     * Search passengers by name (first or last name contains keyword)
     * @param keyword search term
     * @return List of matching passengers
     */
    @Query("SELECT p FROM Passenger p WHERE LOWER(p.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Passenger> searchByName(@Param("keyword") String keyword);

    /**
     * Find passengers by meal preference
     * @param mealPreference meal preference type
     * @return List of passengers
     */
    List<Passenger> findByMealPreference(String mealPreference);

    /**
     * Check if passenger exists by first and last name
     * @param firstName first name
     * @param lastName last name
     * @return true if exists
     */
    boolean existsByFirstNameAndLastName(String firstName, String lastName);

    /**
     * Count passengers by country
     * @param country country name
     * @return count
     */
    long countByCountry(String country);

    /**
     * Find passengers with phone number
     * @param phoneNumber phone number
     * @return Optional<Passenger>
     */
    Optional<Passenger> findByPhoneNumber(String phoneNumber);
}
