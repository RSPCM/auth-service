package com.example.authservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = "table_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime datetimeCreated;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime datetimeUpdated;

    @CreatedBy
    private Long createBy;
}
