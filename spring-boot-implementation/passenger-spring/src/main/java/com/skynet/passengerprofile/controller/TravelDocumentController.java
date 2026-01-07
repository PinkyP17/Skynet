package com.skynet.passengerprofile.controller;

import com.skynet.passengerprofile.model.TravelDocument;
import com.skynet.passengerprofile.service.TravelDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * TravelDocumentController - REST API endpoints for Travel Document management
 * 
 * Base URL: /api/passenger-profile/documents
 * 
 * Functionality 3: Upload Travel Documents
 * Enhancement 3: Document Expiry Alerts
 */
@RestController
@RequestMapping("/documents")
@CrossOrigin(origins = "*")
public class TravelDocumentController {

    @Autowired
    private TravelDocumentService travelDocumentService;

    // ========================================
    // FUNCTIONALITY 3: UPLOAD TRAVEL DOCUMENTS
    // ========================================

    /**
     * Get document by ID
     * GET /documents/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getDocumentById(@PathVariable Long id) {
        TravelDocument document = travelDocumentService.getDocumentById(id);
        
        if (document == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Document not found with ID: " + id));
        }
        
        return ResponseEntity.ok(document);
    }

    /**
     * Get all documents for a passenger
     * GET /documents/passenger/{passengerId}
     */
    @GetMapping("/passenger/{passengerId}")
    public ResponseEntity<List<TravelDocument>> getDocumentsByPassenger(@PathVariable Long passengerId) {
        List<TravelDocument> documents = travelDocumentService.getDocumentsByPassengerId(passengerId);
        return ResponseEntity.ok(documents);
    }

    /**
     * Upload/create new travel document
     * POST /documents
     * Body: TravelDocument JSON
     */
    @PostMapping
    public ResponseEntity<?> uploadDocument(@RequestBody TravelDocument document) {
        try {
            TravelDocument created = travelDocumentService.uploadDocument(document);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Update travel document
     * PUT /documents/{id}
     * Body: TravelDocument JSON with fields to update
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDocument(@PathVariable Long id, 
                                           @RequestBody TravelDocument document) {
        try {
            TravelDocument updated = travelDocumentService.updateDocument(id, document);
            
            if (updated == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Document not found with ID: " + id));
            }
            
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Delete travel document
     * DELETE /documents/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDocument(@PathVariable Long id) {
        boolean deleted = travelDocumentService.deleteDocument(id);
        
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Document not found with ID: " + id));
        }
        
        return ResponseEntity.ok(Map.of("message", "Document deleted successfully"));
    }

    /**
     * Get documents by type for a passenger
     * GET /documents/passenger/{passengerId}/type/{type}
     * Example: /documents/passenger/1/type/Passport
     */
    @GetMapping("/passenger/{passengerId}/type/{type}")
    public ResponseEntity<List<TravelDocument>> getDocumentsByType(
            @PathVariable Long passengerId, 
            @PathVariable String type) {
        List<TravelDocument> documents = travelDocumentService.getDocumentsByType(passengerId, type);
        return ResponseEntity.ok(documents);
    }

    /**
     * Check if passenger has a specific document type
     * GET /documents/passenger/{passengerId}/has/{type}
     */
    @GetMapping("/passenger/{passengerId}/has/{type}")
    public ResponseEntity<?> hasDocumentType(@PathVariable Long passengerId, 
                                            @PathVariable String type) {
        boolean hasDocument = travelDocumentService.hasDocumentType(passengerId, type);
        return ResponseEntity.ok(Map.of(
            "passengerId", passengerId,
            "documentType", type,
            "hasDocument", hasDocument
        ));
    }

    // ========================================
    // ENHANCEMENT 3: DOCUMENT EXPIRY ALERTS
    // ========================================

    /**
     * Get documents expiring soon for a passenger (within 6 months)
     * GET /documents/passenger/{passengerId}/expiring-soon
     * Enhancement: Document Expiry Alerts
     */
    @GetMapping("/passenger/{passengerId}/expiring-soon")
    public ResponseEntity<List<TravelDocument>> getExpiringSoonDocuments(@PathVariable Long passengerId) {
        List<TravelDocument> documents = travelDocumentService.getExpiringSoonDocuments(passengerId);
        return ResponseEntity.ok(documents);
    }

    /**
     * Get expired documents for a passenger
     * GET /documents/passenger/{passengerId}/expired
     */
    @GetMapping("/passenger/{passengerId}/expired")
    public ResponseEntity<List<TravelDocument>> getExpiredDocuments(@PathVariable Long passengerId) {
        List<TravelDocument> documents = travelDocumentService.getExpiredDocuments(passengerId);
        return ResponseEntity.ok(documents);
    }

    /**
     * Get document expiry status (categorized)
     * GET /documents/passenger/{passengerId}/expiry-status
     * Enhancement: Returns documents grouped by Valid/Expiring Soon/Expired
     */
    @GetMapping("/passenger/{passengerId}/expiry-status")
    public ResponseEntity<Map<String, List<TravelDocument>>> getDocumentExpiryStatus(
            @PathVariable Long passengerId) {
        Map<String, List<TravelDocument>> statusMap = 
            travelDocumentService.getDocumentExpiryStatus(passengerId);
        return ResponseEntity.ok(statusMap);
    }

    /**
     * Get expiry alert count for a passenger
     * GET /documents/passenger/{passengerId}/alert-count
     * Enhancement: Returns count for badge display
     */
    @GetMapping("/passenger/{passengerId}/alert-count")
    public ResponseEntity<?> getExpiryAlertCount(@PathVariable Long passengerId) {
        int alertCount = travelDocumentService.getExpiryAlertCount(passengerId);
        boolean hasAlerts = travelDocumentService.hasExpiryAlerts(passengerId);
        
        return ResponseEntity.ok(Map.of(
            "passengerId", passengerId,
            "alertCount", alertCount,
            "hasAlerts", hasAlerts
        ));
    }

    /**
     * Get detailed expiry alert information
     * GET /documents/passenger/{passengerId}/expiry-alerts
     * Enhancement: Complete alert data for UI display
     */
    @GetMapping("/passenger/{passengerId}/expiry-alerts")
    public ResponseEntity<List<Map<String, Object>>> getExpiryAlertDetails(
            @PathVariable Long passengerId) {
        List<Map<String, Object>> alertDetails = 
            travelDocumentService.getExpiryAlertDetails(passengerId);
        return ResponseEntity.ok(alertDetails);
    }

    /**
     * Check if passenger has any expiry alerts
     * GET /documents/passenger/{passengerId}/has-alerts
     */
    @GetMapping("/passenger/{passengerId}/has-alerts")
    public ResponseEntity<?> hasExpiryAlerts(@PathVariable Long passengerId) {
        boolean hasAlerts = travelDocumentService.hasExpiryAlerts(passengerId);
        int count = travelDocumentService.getExpiryAlertCount(passengerId);
        
        return ResponseEntity.ok(Map.of(
            "passengerId", passengerId,
            "hasAlerts", hasAlerts,
            "alertCount", count
        ));
    }

    // ========================================
    // ADMIN ENDPOINTS (All Passengers)
    // ========================================

    /**
     * Get all expiring documents (across all passengers)
     * GET /documents/all/expiring-soon
     * Admin feature
     */
    @GetMapping("/all/expiring-soon")
    public ResponseEntity<List<TravelDocument>> getAllExpiringSoonDocuments() {
        List<TravelDocument> documents = travelDocumentService.getAllExpiringSoonDocuments();
        return ResponseEntity.ok(documents);
    }

    /**
     * Get all expired documents (across all passengers)
     * GET /documents/all/expired
     * Admin feature
     */
    @GetMapping("/all/expired")
    public ResponseEntity<List<TravelDocument>> getAllExpiredDocuments() {
        List<TravelDocument> documents = travelDocumentService.getAllExpiredDocuments();
        return ResponseEntity.ok(documents);
    }

    // ========================================
    // STATISTICS & UTILITIES
    // ========================================

    /**
     * Get document count for a passenger
     * GET /documents/passenger/{passengerId}/count
     */
    @GetMapping("/passenger/{passengerId}/count")
    public ResponseEntity<?> getDocumentCount(@PathVariable Long passengerId) {
        long count = travelDocumentService.getDocumentCount(passengerId);
        return ResponseEntity.ok(Map.of(
            "passengerId", passengerId,
            "documentCount", count
        ));
    }

    /**
     * Delete all documents for a passenger
     * DELETE /documents/passenger/{passengerId}/all
     */
    @DeleteMapping("/passenger/{passengerId}/all")
    public ResponseEntity<?> deleteAllDocuments(@PathVariable Long passengerId) {
        travelDocumentService.deleteAllDocumentsForPassenger(passengerId);
        return ResponseEntity.ok(Map.of(
            "message", "All documents deleted for passenger " + passengerId
        ));
    }
}
