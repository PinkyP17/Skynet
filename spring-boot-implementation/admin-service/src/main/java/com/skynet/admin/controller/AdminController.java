package com.skynet.admin.controller;

import com.skynet.admin.model.AuditLog;
import com.skynet.admin.model.Feedback;
import com.skynet.admin.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
/* * We set this to "/" because the context-path in application.properties 
 * is already handling the "/api/admin" prefix.
 */
@RequestMapping("/") 
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // 1. Generate Sales Report
    // Access at: http://localhost:8083/api/admin/reports/sales
    @GetMapping("/reports/sales")
    public ResponseEntity<String> getSalesReport() {
        return ResponseEntity.ok(adminService.generateSalesReport());
    }

    // 2. List Passenger List 
    // Access at: http://localhost:8083/api/admin/passengers
    @GetMapping("/passengers")
    public ResponseEntity<List<String>> getPassengers() {
        // Currently returning a mock list as a placeholder
        return ResponseEntity.ok(List.of("Passenger 101 - Seat 1A", "Passenger 102 - Seat 1B"));
    }

    // 3. Customer Feedback Review
    // Access at: http://localhost:8083/api/admin/feedback
    @PostMapping("/feedback")
    public ResponseEntity<Feedback> submitFeedback(@RequestBody Feedback feedback) {
        return ResponseEntity.ok(adminService.submitFeedback(feedback));
    }

    // Access at: http://localhost:8083/api/admin/feedback/category/{category}
    @GetMapping("/feedback/category/{category}")
    public ResponseEntity<List<Feedback>> getFeedbackByCategory(@PathVariable String category) {
        return ResponseEntity.ok(adminService.getFeedbackByCategory(category));
    }

    // 4. System Audit Log
    // Access at: http://localhost:8083/api/admin/logs
    @GetMapping("/logs")
    public ResponseEntity<List<AuditLog>> getAuditLogs() {
        return ResponseEntity.ok(adminService.getAuditLogs());
    }
}