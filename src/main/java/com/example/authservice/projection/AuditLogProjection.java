package com.example.authservice.projection;

import java.time.LocalDateTime;
import java.util.UUID;

public interface AuditLogProjection {
    Long getReqLogId();
    String getRequestId();
    String getHttpMethod();
    String getRequestUri();
    String getQueryString();
    String getRemoteAddress();
    String getUserAgent();
    Integer getResponseStatus();
    Long getDurationMs();
    LocalDateTime getRequestTime();

    UUID getAuditLogId();
    String getEntityType();
    String getEntityId();
    String getAction();
    String getDiffJson();
    LocalDateTime getAuditTime();

    UUID getUserId();
    String getUsername();
}
