package com.example.authservice.log.entity;

import com.example.authservice.entity.BaseScale;
import com.example.authservice.log.enums.AuditAction;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.util.UUID;

@Entity
@Table(name = "audit_log")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AuditLog extends BaseScale {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String requestId;

    // User
    private UUID userId;
    private String username;

    // Entity Info
    private String entityType;
    private String entityId;

    // Action Type (CREATE, UPDATE, DELETE)
    @Enumerated(EnumType.STRING)
    private AuditAction action;

    @JdbcTypeCode(value = org.hibernate.type.SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode beforeJson;

    @JdbcTypeCode(value = org.hibernate.type.SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode afterJson;

    @JdbcTypeCode(value = org.hibernate.type.SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode diffJson;
}
