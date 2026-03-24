package com.example.authservice.mapper;

import com.example.authservice.dto.response.AuditLogResponse;
import com.example.authservice.projection.AuditLogProjection;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuditLogMapper {
    private final ObjectMapper objectMapper;

    public AuditLogResponse toResponse(AuditLogProjection projection) {
        return AuditLogResponse.builder()
                .requestId(projection.getRequestId())
                .serviceName(projection.getServiceName())
                .auditLogId(projection.getAuditLogId())
                .entityType(projection.getEntityType())
                .entityId(projection.getEntityId())
                .action(projection.getAction())
                .diffJson(parseDiffJson(projection.getDiffJson()))
                .auditTime(projection.getAuditTime())
                .userId(projection.getUserId())
                .username(projection.getUsername())
                .build();
    }

    private JsonNode parseDiffJson(String diffJson) {
        if (diffJson == null || diffJson.isBlank()) return null;
        try {
            return objectMapper.readTree(diffJson);
        } catch (Exception e) {
            log.warn("diffJson parse qilishda xatolik: {}", e.getMessage());
            return null;
        }
    }
}
