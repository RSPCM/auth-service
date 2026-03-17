package com.example.authservice.log.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuditLogResponse {

    private Long reqLogId;
    private String requestId;
    private String httpMethod;
    private String requestUri;
    private String queryString;
    private String remoteAddress;
    private String userAgent;
    private Integer responseStatus;
    private Long durationMs;
    private LocalDateTime requestTime;

    private UUID auditLogId;
    private String entityType;
    private String entityId;
    private String action;
    private JsonNode diffJson;
    private LocalDateTime auditTime;

    private UUID userId;
    private String username;
}
