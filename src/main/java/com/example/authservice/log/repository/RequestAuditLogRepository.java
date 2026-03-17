package com.example.authservice.log.repository;

import com.example.authservice.log.entity.RequestAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestAuditLogRepository extends JpaRepository<RequestAuditLog, Long> {
}
