package com.example.authservice.log.entity;

import com.example.authservice.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "request_audit_log")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestAuditLog extends BaseEntity {
    @Column(nullable = false, length = 100)
    private String requestId;

    @Column(nullable = false, length = 16)
    private String httpMethod;

    @Column(nullable = false, length = 512)
    private String requestUri;

    @Column(length = 1024)
    private String queryString;

    @Column(length = 64)
    private String remoteAddress;

    @Column(length = 256)
    private String userAgent;

    private Integer responseStatus;

    private Long durationMs;
}
