package com.example.authservice.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.example.authservice.entity.OutboxEvent;
import com.example.authservice.enums.OutboxStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {

    List<OutboxEvent> findByStatusAndNextAttemptAtLessThanEqualOrderByDatetimeCreatedAsc(
            OutboxStatus status,
            LocalDateTime nextAttemptAt,
            Pageable pageable
    );
}

