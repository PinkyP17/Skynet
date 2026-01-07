package com.skynet.passengerprofile.repository;

import com.skynet.passengerprofile.model.TravelDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TravelDocumentRepository - Data access layer for TravelDocument entity
 * 
 * Supports Document Expiry Alerts enhancement
 */
@Repository
public interface TravelDocumentRepository extends JpaRepository<TravelDocument, Long> {

    /**
     * Find all documents for a specific passenger
     * @param passengerId passenger ID
     * @return List of travel documents
     */
    List<TravelDocument> findByPassengerId(Long passengerId);

    /**
     * Find documents by type for a passenger
     * @param passengerId passenger ID
     * @param documentType document type (e.g., "Passport", "Visa")
     * @return List of travel documents
     */
    List<TravelDocument> findByPassengerIdAndDocumentType(Long passengerId, String documentType);

    /**
     * Find all documents by type
     * @param documentType document type
     * @return List of travel documents
     */
    List<TravelDocument> findByDocumentType(String documentType);

    /**
     * Find documents by issuing country
     * @param issuingCountry country code
     * @return List of travel documents
     */
    List<TravelDocument> findByIssuingCountry(String issuingCountry);

    /**
     * Find documents expiring before a certain date
     * Enhancement: Document Expiry Alerts
     * @param date expiry date threshold (format: "YYYY-MM-DD")
     * @param currentDate current date (format: "YYYY-MM-DD")
     * @return List of expiring documents
     */
    @Query("SELECT td FROM TravelDocument td WHERE td.expiryDate <= :date AND td.expiryDate >= :currentDate")
    List<TravelDocument> findExpiringBefore(@Param("date") String date, @Param("currentDate") String currentDate);

    /**
     * Find documents expiring within 6 months for a passenger
     * Enhancement: Document Expiry Alerts
     * @param passengerId passenger ID
     * @param sixMonthsLater date 6 months from now
     * @param currentDate current date (format: "YYYY-MM-DD")
     * @return List of documents expiring soon
     */
    @Query("SELECT td FROM TravelDocument td WHERE td.passengerId = :passengerId " +
           "AND td.expiryDate <= :sixMonthsLater AND td.expiryDate >= :currentDate")
    List<TravelDocument> findExpiringSoonForPassenger(
        @Param("passengerId") Long passengerId, 
        @Param("sixMonthsLater") String sixMonthsLater,
        @Param("currentDate") String currentDate
    );

    /**
     * Find expired documents for a passenger
     * @param passengerId passenger ID
     * @param currentDate current date (format: "YYYY-MM-DD")
     * @return List of expired documents
     */
    @Query("SELECT td FROM TravelDocument td WHERE td.passengerId = :passengerId " +
           "AND td.expiryDate < :currentDate")
    List<TravelDocument> findExpiredForPassenger(@Param("passengerId") Long passengerId, @Param("currentDate") String currentDate);

    /**
     * Find all expired documents
     * @param currentDate current date (format: "YYYY-MM-DD")
     * @return List of expired documents
     */
    @Query("SELECT td FROM TravelDocument td WHERE td.expiryDate < :currentDate")
    List<TravelDocument> findAllExpired(@Param("currentDate") String currentDate);

    /**
     * Count documents for a passenger
     * @param passengerId passenger ID
     * @return count
     */
    long countByPassengerId(Long passengerId);

    /**
     * Check if passenger has a specific document type
     * @param passengerId passenger ID
     * @param documentType document type
     * @return true if exists
     */
    boolean existsByPassengerIdAndDocumentType(Long passengerId, String documentType);

    /**
     * Find document by document number
     * @param documentNumber document number
     * @return List of documents (should be unique, but returns list for safety)
     */
    List<TravelDocument> findByDocumentNumber(String documentNumber);

    /**
     * Delete all documents for a passenger
     * @param passengerId passenger ID
     */
    void deleteByPassengerId(Long passengerId);
}
