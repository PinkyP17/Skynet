package com.skynet.passengerprofile.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * TravelDocument Entity - represents passenger travel documents
 *
 * Maps to the 'travel_documents' table in the database
 */
@Entity
@Table(name = "travel_documents")
public class TravelDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_passenger", insertable = false, updatable = false)
    private Long passengerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_passenger", nullable = false)
    @JsonIgnore
    private Passenger passenger;

    @Column(name = "document_type", nullable = false)
    private String documentType;

    @Column(name = "document_number", nullable = false)
    private String documentNumber;

    @Column(name = "issue_date")
    private String issueDate;

    @Column(name = "expiry_date")
    private String expiryDate;

    @Column(name = "issuing_country")
    private String issuingCountry;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "created_date")
    private String createdDate;

    // Constructors
    public TravelDocument() {}

    public TravelDocument(Long id, Long passengerId, String documentType, String documentNumber,
                          String issueDate, String expiryDate, String issuingCountry,
                          String filePath, String createdDate) {
        this.id = id;
        this.passengerId = passengerId;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.issuingCountry = issuingCountry;
        this.filePath = filePath;
        this.createdDate = createdDate;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPassengerId() { return passengerId; }
    public void setPassengerId(Long passengerId) { this.passengerId = passengerId; }

    public Passenger getPassenger() { return passenger; }
    public void setPassenger(Passenger passenger) { this.passenger = passenger; }

    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }

    public String getDocumentNumber() { return documentNumber; }
    public void setDocumentNumber(String documentNumber) { this.documentNumber = documentNumber; }

    public String getIssueDate() { return issueDate; }
    public void setIssueDate(String issueDate) { this.issueDate = issueDate; }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }

    public String getIssuingCountry() { return issuingCountry; }
    public void setIssuingCountry(String issuingCountry) { this.issuingCountry = issuingCountry; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }

    // Helper methods
    public boolean isExpiringSoon() {
        if (expiryDate == null || expiryDate.isEmpty()) return false;
        try {
            LocalDate expiry = LocalDate.parse(expiryDate);
            LocalDate today = LocalDate.now();
            LocalDate sixMonthsLater = today.plusMonths(6);
            return expiry.isBefore(sixMonthsLater) && expiry.isAfter(today);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isExpired() {
        if (expiryDate == null || expiryDate.isEmpty()) return false;
        try {
            LocalDate expiry = LocalDate.parse(expiryDate);
            return expiry.isBefore(LocalDate.now());
        } catch (Exception e) {
            return false;
        }
    }

    public long getDaysUntilExpiry() {
        if (expiryDate == null || expiryDate.isEmpty()) return -1;
        try {
            LocalDate expiry = LocalDate.parse(expiryDate);
            LocalDate today = LocalDate.now();
            if (expiry.isBefore(today)) return -1;
            return ChronoUnit.DAYS.between(today, expiry);
        } catch (Exception e) {
            return -1;
        }
    }

    public String getExpiryStatus() {
        if (isExpired()) return "Expired";
        else if (isExpiringSoon()) return "Expiring Soon";
        else return "Valid";
    }
}
