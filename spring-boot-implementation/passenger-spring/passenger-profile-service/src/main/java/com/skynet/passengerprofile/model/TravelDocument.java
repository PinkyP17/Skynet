package com.skynet.passengerprofile.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * TravelDocument Entity - represents passenger travel documents
 * 
 * Maps to the 'travel_documents' table in the database
 */
@Entity
@Table(name = "travel_documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TravelDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_passenger", insertable = false, updatable = false)
    private Long passengerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_passenger", nullable = false)
    @JsonIgnore // Prevent infinite recursion in JSON serialization
    private Passenger passenger;

    @Column(name = "document_type", nullable = false)
    private String documentType; // "Passport", "Visa", "ID Card", "Driver License"

    @Column(name = "document_number", nullable = false)
    private String documentNumber;

    @Column(name = "issue_date")
    private String issueDate; // Format: "YYYY-MM-DD"

    @Column(name = "expiry_date")
    private String expiryDate; // Format: "YYYY-MM-DD"

    @Column(name = "issuing_country")
    private String issuingCountry;

    @Column(name = "file_path")
    private String filePath; // Optional: path to uploaded document file

    @Column(name = "created_date")
    private String createdDate;

    // Helper methods

    /**
     * Check if document is expiring soon (within 6 months)
     * Enhancement: Document Expiry Alerts
     * @return true if expiring within 6 months
     */
    public boolean isExpiringSoon() {
        if (expiryDate == null || expiryDate.isEmpty()) {
            return false;
        }

        try {
            LocalDate expiry = LocalDate.parse(expiryDate);
            LocalDate today = LocalDate.now();
            LocalDate sixMonthsLater = today.plusMonths(6);

            return expiry.isBefore(sixMonthsLater) && expiry.isAfter(today);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if document is expired
     * @return true if expired
     */
    public boolean isExpired() {
        if (expiryDate == null || expiryDate.isEmpty()) {
            return false;
        }

        try {
            LocalDate expiry = LocalDate.parse(expiryDate);
            return expiry.isBefore(LocalDate.now());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get days until expiry
     * @return number of days, or -1 if expired/invalid
     */
    public long getDaysUntilExpiry() {
        if (expiryDate == null || expiryDate.isEmpty()) {
            return -1;
        }

        try {
            LocalDate expiry = LocalDate.parse(expiryDate);
            LocalDate today = LocalDate.now();
            
            if (expiry.isBefore(today)) {
                return -1; // Already expired
            }
            
            return ChronoUnit.DAYS.between(today, expiry);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Get expiry status for alerts
     * @return "Expired", "Expiring Soon", or "Valid"
     */
    public String getExpiryStatus() {
        if (isExpired()) {
            return "Expired";
        } else if (isExpiringSoon()) {
            return "Expiring Soon";
        } else {
            return "Valid";
        }
    }
}
