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
                .reqLogId(projection.getReqLogId())
                .requestId(projection.getRequestId())
                .httpMethod(projection.getHttpMethod())
                .requestUri(projection.getRequestUri())
                .queryString(projection.getQueryString())
                .remoteAddress(projection.getRemoteAddress())
                .userAgent(projection.getUserAgent())
                .responseStatus(projection.getResponseStatus())
                .durationMs(projection.getDurationMs())
                .requestTime(projection.getRequestTime())
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
