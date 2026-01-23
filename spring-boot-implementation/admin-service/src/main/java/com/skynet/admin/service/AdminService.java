package com.skynet.admin.service;

import com.skynet.admin.model.AuditLog;
import com.skynet.admin.model.Feedback;
import com.skynet.admin.repository.AuditLogRepository;
import com.skynet.admin.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AdminService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    public String generateSalesReport() {
        logAction("SYSTEM", "GENERATE_REPORT", "SALES", "ALL");
        return "Sales Report Generated: Total Revenue RM 50,000";
    }

    public List<Feedback> getFeedbackByCategory(String category) {
        return feedbackRepository.findByCategory(category);
    }

    public Feedback submitFeedback(Feedback feedback) {
        if (feedback.getCreatedAt() == null) {
            feedback.setCreatedAt(LocalDateTime.now().toString());
        }
        return feedbackRepository.save(feedback);
    }

    public void logAction(String adminId, String action, String targetType, String targetId) {
        AuditLog log = new AuditLog();
        log.setAdminId(adminId);
        log.setAction(action);
        log.setTargetType(targetType);
        log.setTargetId(targetId);
        log.setTimestamp(LocalDateTime.now());
        auditLogRepository.save(log);
    }

    public List<AuditLog> getAuditLogs() {
        return auditLogRepository.findAll();
    }
    
    public List<AuditLog> getLogsByAdmin(String adminId) {
        return auditLogRepository.findByAdminId(adminId);
    }
}