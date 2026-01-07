package com.skynet.common.service;

import java.util.List;

public interface IAdminService {
    void generateReports(String reportType); // Maybe return byte[] or File
    List<String> viewAuditLogs(); // Return list of log strings or LogEntry objects
    void manageSystemFeedback();
}
