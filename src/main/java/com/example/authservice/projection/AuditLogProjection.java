package com.example.authservice.projection;

import java.time.LocalDateTime;
import java.util.UUID;

public interface AuditLogProjection {
    UUID getAuditLogId();
    String getRequestId();
    String getServiceName();
    String getEntityType();
    String getEntityId();
    String getAction();
    String getDiffJson();
    LocalDateTime getAuditTime();

    UUID getUserId();
    String getUsername();
}
