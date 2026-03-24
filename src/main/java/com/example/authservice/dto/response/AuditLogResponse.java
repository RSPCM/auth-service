package com.example.authservice.dto.response;

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

    private String requestId;
    private String serviceName;

    private UUID auditLogId;
    private String entityType;
    private String entityId;
    private String action;
    private JsonNode diffJson;
    private LocalDateTime auditTime;

    private UUID userId;
    private String username;
}
