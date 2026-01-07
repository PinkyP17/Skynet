package com.skynet.passengerprofile.service;

import com.skynet.passengerprofile.model.TravelDocument;
import com.skynet.passengerprofile.repository.TravelDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * TravelDocumentService - Business logic for Travel Document management
 * 
 * Functionality 3: Upload Travel Documents
 * Enhancement 3: Document Expiry Alerts
 */
@Service
@Transactional
public class TravelDocumentService {

    @Autowired
    private TravelDocumentRepository travelDocumentRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // ========================================
    // FUNCTIONALITY 3: UPLOAD TRAVEL DOCUMENTS
    // ========================================

    /**
     * Get document by ID
     * @param id document ID
     * @return TravelDocument or null
     */
    public TravelDocument getDocumentById(Long id) {
        return travelDocumentRepository.findById(id).orElse(null);
    }

    /**
     * Get all documents for a passenger
     * @param passengerId passenger ID
     * @return List of travel documents
     */
    public List<TravelDocument> getDocumentsByPassengerId(Long passengerId) {
        return travelDocumentRepository.findByPassengerId(passengerId);
    }

    /**
     * Upload/create new travel document
     * @param document travel document object
     * @return saved document
     */
    public TravelDocument uploadDocument(TravelDocument document) {
        // Set created date if not provided
        if (document.getCreatedDate() == null) {
            document.setCreatedDate(LocalDate.now().format(DATE_FORMATTER));
        }
        
        // Validate dates
        validateDocumentDates(document);
        
        return travelDocumentRepository.save(document);
    }

    /**
     * Update travel document
     * @param id document ID
     * @param updatedDocument updated document data
     * @return updated document or null if not found
     */
    public TravelDocument updateDocument(Long id, TravelDocument updatedDocument) {
        Optional<TravelDocument> existingOpt = travelDocumentRepository.findById(id);
        
        if (existingOpt.isEmpty()) {
            return null;
        }
        
        TravelDocument existing = existingOpt.get();
        
        // Update fields
        if (updatedDocument.getDocumentType() != null) {
            existing.setDocumentType(updatedDocument.getDocumentType());
        }
        if (updatedDocument.getDocumentNumber() != null) {
            existing.setDocumentNumber(updatedDocument.getDocumentNumber());
        }
        if (updatedDocument.getIssueDate() != null) {
            existing.setIssueDate(updatedDocument.getIssueDate());
        }
        if (updatedDocument.getExpiryDate() != null) {
            existing.setExpiryDate(updatedDocument.getExpiryDate());
        }
        if (updatedDocument.getIssuingCountry() != null) {
            existing.setIssuingCountry(updatedDocument.getIssuingCountry());
        }
        if (updatedDocument.getFilePath() != null) {
            existing.setFilePath(updatedDocument.getFilePath());
        }
        
        // Validate dates
        validateDocumentDates(existing);
        
        return travelDocumentRepository.save(existing);
    }

    /**
     * Delete travel document
     * @param id document ID
     * @return true if deleted, false if not found
     */
    public boolean deleteDocument(Long id) {
        if (travelDocumentRepository.existsById(id)) {
            travelDocumentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Get documents by type for a passenger
     * @param passengerId passenger ID
     * @param documentType document type
     * @return List of documents
     */
    public List<TravelDocument> getDocumentsByType(Long passengerId, String documentType) {
        return travelDocumentRepository.findByPassengerIdAndDocumentType(passengerId, documentType);
    }

    /**
     * Check if passenger has a specific document type
     * @param passengerId passenger ID
     * @param documentType document type
     * @return true if exists
     */
    public boolean hasDocumentType(Long passengerId, String documentType) {
        return travelDocumentRepository.existsByPassengerIdAndDocumentType(passengerId, documentType);
    }

    // ========================================
    // ENHANCEMENT 3: DOCUMENT EXPIRY ALERTS
    // ========================================

    /**
     * Get documents expiring soon for a passenger (within 6 months)
     * Enhancement: Document Expiry Alerts
     * @param passengerId passenger ID
     * @return List of documents expiring soon
     */
    public List<TravelDocument> getExpiringSoonDocuments(Long passengerId) {
        String sixMonthsLater = LocalDate.now().plusMonths(6).format(DATE_FORMATTER);
        String currentDate = LocalDate.now().format(DATE_FORMATTER);
        return travelDocumentRepository.findExpiringSoonForPassenger(passengerId, sixMonthsLater, currentDate);
    }

    /**
     * Get expired documents for a passenger
     * @param passengerId passenger ID
     * @return List of expired documents
     */
    public List<TravelDocument> getExpiredDocuments(Long passengerId) {
        String currentDate = LocalDate.now().format(DATE_FORMATTER);
        return travelDocumentRepository.findExpiredForPassenger(passengerId, currentDate);
    }

    /**
     * Get all expiring documents (across all passengers)
     * Admin feature for monitoring
     * @return List of expiring documents
     */
    public List<TravelDocument> getAllExpiringSoonDocuments() {
        String sixMonthsLater = LocalDate.now().plusMonths(6).format(DATE_FORMATTER);
        String currentDate = LocalDate.now().format(DATE_FORMATTER);
        return travelDocumentRepository.findExpiringBefore(sixMonthsLater, currentDate);
    }

    /**
     * Get all expired documents (across all passengers)
     * @return List of expired documents
     */
    public List<TravelDocument> getAllExpiredDocuments() {
        String currentDate = LocalDate.now().format(DATE_FORMATTER);
        return travelDocumentRepository.findAllExpired(currentDate);
    }

    /**
     * Get document expiry status
     * Enhancement: Returns categorized alerts
     * @param passengerId passenger ID
     * @return Map of status → documents
     */
    public java.util.Map<String, List<TravelDocument>> getDocumentExpiryStatus(Long passengerId) {
        List<TravelDocument> allDocs = travelDocumentRepository.findByPassengerId(passengerId);
        
        java.util.Map<String, List<TravelDocument>> statusMap = new java.util.HashMap<>();
        
        statusMap.put("Valid", allDocs.stream()
            .filter(doc -> !doc.isExpired() && !doc.isExpiringSoon())
            .collect(Collectors.toList()));
        
        statusMap.put("Expiring Soon", allDocs.stream()
            .filter(TravelDocument::isExpiringSoon)
            .collect(Collectors.toList()));
        
        statusMap.put("Expired", allDocs.stream()
            .filter(TravelDocument::isExpired)
            .collect(Collectors.toList()));
        
        return statusMap;
    }

    /**
     * Get expiry alert count for a passenger
     * Returns number of documents needing attention
     * @param passengerId passenger ID
     * @return count of expiring + expired documents
     */
    public int getExpiryAlertCount(Long passengerId) {
        List<TravelDocument> expiringSoon = getExpiringSoonDocuments(passengerId);
        List<TravelDocument> expired = getExpiredDocuments(passengerId);
        return expiringSoon.size() + expired.size();
    }

    /**
     * Check if passenger has any expiry alerts
     * @param passengerId passenger ID
     * @return true if has expiring or expired documents
     */
    public boolean hasExpiryAlerts(Long passengerId) {
        return getExpiryAlertCount(passengerId) > 0;
    }

    /**
     * Get detailed expiry information for UI
     * Enhancement: Provides all data needed for expiry alert UI
     * @param passengerId passenger ID
     * @return List of maps with document details and expiry info
     */
    public List<java.util.Map<String, Object>> getExpiryAlertDetails(Long passengerId) {
        List<TravelDocument> alertDocs = new java.util.ArrayList<>();
        alertDocs.addAll(getExpiringSoonDocuments(passengerId));
        alertDocs.addAll(getExpiredDocuments(passengerId));
        
        return alertDocs.stream().map(doc -> {
            java.util.Map<String, Object> details = new java.util.HashMap<>();
            details.put("documentId", doc.getId());
            details.put("documentType", doc.getDocumentType());
            details.put("documentNumber", doc.getDocumentNumber());
            details.put("expiryDate", doc.getExpiryDate());
            details.put("daysUntilExpiry", doc.getDaysUntilExpiry());
            details.put("status", doc.getExpiryStatus());
            details.put("isExpired", doc.isExpired());
            details.put("isExpiringSoon", doc.isExpiringSoon());
            return details;
        }).collect(Collectors.toList());
    }

    // ========================================
    // VALIDATION & HELPER METHODS
    // ========================================

    /**
     * Validate document dates
     * @param document document to validate
     * @throws IllegalArgumentException if dates are invalid
     */
    private void validateDocumentDates(TravelDocument document) {
        if (document.getIssueDate() != null && document.getExpiryDate() != null) {
            try {
                LocalDate issueDate = LocalDate.parse(document.getIssueDate(), DATE_FORMATTER);
                LocalDate expiryDate = LocalDate.parse(document.getExpiryDate(), DATE_FORMATTER);
                
                if (expiryDate.isBefore(issueDate)) {
                    throw new IllegalArgumentException("Expiry date cannot be before issue date");
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid date format. Use YYYY-MM-DD");
            }
        }
    }

    /**
     * Get total document count for a passenger
     * @param passengerId passenger ID
     * @return count
     */
    public long getDocumentCount(Long passengerId) {
        return travelDocumentRepository.countByPassengerId(passengerId);
    }

    /**
     * Delete all documents for a passenger
     * @param passengerId passenger ID
     */
    public void deleteAllDocumentsForPassenger(Long passengerId) {
        travelDocumentRepository.deleteByPassengerId(passengerId);
    }
}
