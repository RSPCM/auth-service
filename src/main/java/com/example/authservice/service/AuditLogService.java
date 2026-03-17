package com.example.authservice.service;

import com.example.authservice.entity.User;
import com.example.authservice.exceptions.ErrorCodes;
import com.example.authservice.exceptions.entity.ErrorMessageException;
import com.example.authservice.log.enums.AuditAction;
import com.example.authservice.log.entity.AuditLog;
import com.example.authservice.log.dto.AuditLogResponse;
import com.example.authservice.log.RequestContext;
import com.example.authservice.log.mapper.AuditLogMapper;
import com.example.authservice.repository.AuditLogRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import static com.example.authservice.util.SpringSecurityUtil.getCurrentUser;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;
    private final AuditLogMapper auditLogMapper;

    public void log(
            Object entity,
            AuditAction action,
            Object beforeObj,
            Object afterObj
    ) {
        User currentUser = getCurrentUser();

        String entityType = extractEntityType(entity);
        String entityId = extractEntityId(entity);

        JsonNode beforeJson = toJsonNode(beforeObj);
        JsonNode afterJson = toJsonNode(afterObj);
        JsonNode diffJson = generateDiff(beforeJson, afterJson);

        AuditLog auditLog = AuditLog.builder()
                .requestId(RequestContext.getRequestId())
                .userId(currentUser != null ? currentUser.getId() : null)
                .username(currentUser != null ? currentUser.getUsername() : null)
                .entityType(entityType)
                .entityId(entityId)
                .action(action)
                .beforeJson(beforeJson)
                .afterJson(afterJson)
                .diffJson(diffJson)
                .build();

        auditLogRepository.save(auditLog);
    }

    private JsonNode toJsonNode(Object obj) {
        if (obj == null) return null;

        try {
            return objectMapper.valueToTree(obj);
        } catch (Exception e) {
            log.warn("Object ni JsonNode ga o'tkazishda xatolik bo'ldi: {}", e.getMessage());
            return null;
        }
    }

    private JsonNode generateDiff(JsonNode beforeNode, JsonNode afterNode) {
        if (beforeNode == null || afterNode == null || !beforeNode.isObject() || !afterNode.isObject()) {
            return null;
        }

        try {
            Set<String> allKeys = new LinkedHashSet<>();
            Iterator<String> beforeFields = beforeNode.fieldNames();
            while (beforeFields.hasNext()) {
                allKeys.add(beforeFields.next());
            }

            Iterator<String> afterFields = afterNode.fieldNames();
            while (afterFields.hasNext()) {
                allKeys.add(afterFields.next());
            }

            ArrayNode diffArray = objectMapper.createArrayNode();

            for (String key : allKeys) {
                JsonNode beforeValue = beforeNode.get(key);
                JsonNode afterValue = afterNode.get(key);

                if (!Objects.equals(beforeValue, afterValue)) {

                    ObjectNode diffItem = objectMapper.createObjectNode();

                    diffItem.put("field", key);
                    diffItem.set("from", beforeValue == null ? objectMapper.nullNode() : beforeValue);
                    diffItem.set("to", afterValue == null ? objectMapper.nullNode() : afterValue);

                    diffArray.add(diffItem);
                }
            }

            return diffArray.isEmpty() ? null : diffArray;

        } catch (Exception e) {
            log.warn("Diff JSON yaratishda xatolik bo'ldi: {}", e.getMessage());
            return null;
        }
    }

    private String extractEntityType(Object entity) {
        return entity != null ? entity.getClass().getSimpleName() : null;
    }

    private String extractEntityId(Object entity) {
        if (entity == null) {
            return null;
        }

        Class<?> clazz = entity.getClass();

        while (clazz != null) {
            try {
                Field idField = clazz.getDeclaredField("id");
                idField.setAccessible(true);
                Object idValue = idField.get(entity);
                return idValue != null ? idValue.toString() : null;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (IllegalAccessException e) {
                log.warn("Entity id ni olishda access xatoligi bo'ldi: {}", e.getMessage());
                return null;
            } catch (Exception e) {
                log.warn("Entity id ni olishda xatolik bo'ldi: {}", e.getMessage());
                return null;
            }
        }

        return null;
    }

    public ResponseEntity<?> getAuditLogsWithRequestLog(
            String query,
            LocalDate from,
            LocalDate to,
            Pageable pageable
    ) {
        if (from != null && to != null && from.isAfter(to)) {
            throw new ErrorMessageException("'from' must be <= 'to'", ErrorCodes.BadRequest);
        }

        LocalDateTime fromDt = from == null ? null : from.atStartOfDay();
        LocalDateTime toDt = to == null ? null : to.plusDays(1).atStartOfDay();

        Page<AuditLogResponse> result = auditLogRepository
                .getAllLogWithFilter(query, fromDt, toDt, pageable)
                .map(auditLogMapper::toResponse);

        return ResponseEntity.ok(result);
    }
}
