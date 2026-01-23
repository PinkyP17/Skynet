package com.skynet.admin.osgi.api;

import java.util.List;

public interface AdminService {
    // Feature 1: Sales Report (Returns summary string for OSGi console)
    String generateSalesReport();
    
    // Feature 2: Passenger List
    List<String> getPassengerList();
    
    // Feature 3: Feedback
    void submitFeedback(String user, String comment);
    List<String> getFeedback();
    
    // Feature 4: Audit Logs
    List<String> getAuditLogs();
}