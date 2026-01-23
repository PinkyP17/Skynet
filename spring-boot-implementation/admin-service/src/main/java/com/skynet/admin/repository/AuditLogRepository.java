package com.skynet.admin.repository;

import com.skynet.admin.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByAdminId(String adminId);
    List<AuditLog> findByTargetTypeAndTargetId(String targetType, String targetId);
}