package com.example.authservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.example.authservice.config.RabbitMQConfig;
import com.example.authservice.dto.message.TeacherProfileCreateMessage;
import com.example.authservice.entity.OutboxEvent;
import com.example.authservice.enums.OutboxStatus;
import com.example.authservice.repository.OutboxEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxService {

    public static final String TEACHER_PROFILE_CREATE_EVENT = "TEACHER_PROFILE_CREATE";

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;

    @Value("${outbox.publisher.batch-size:50}")
    private int batchSize;

    @Value("${outbox.publisher.max-attempts:10}")
    private int maxAttempts;

    @Value("${outbox.publisher.retry-delay-seconds:30}")
    private int retryDelaySeconds;

    @Transactional
    public void enqueueTeacherProfileCreate(TeacherProfileCreateMessage payload) {
        String jsonPayload;
        try {
            jsonPayload = objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Could not serialize teacher profile create payload", e);
        }

        OutboxEvent outboxEvent = OutboxEvent.builder()
                .eventType(TEACHER_PROFILE_CREATE_EVENT)
                .aggregateId(payload.getAuthUserId().toString())
                .exchangeName(RabbitMQConfig.TEACHER_REGISTRATION_EXCHANGE)
                .routingKey(RabbitMQConfig.TEACHER_RK)
                .payload(jsonPayload)
                .status(OutboxStatus.PENDING)
                .attempts(0)
                .nextAttemptAt(LocalDateTime.now())
                .build();

        outboxEventRepository.save(outboxEvent);
    }

    @Transactional
    public void publishPendingEvents() {
        List<OutboxEvent> events = outboxEventRepository
                .findByStatusAndNextAttemptAtLessThanEqualOrderByDatetimeCreatedAsc(
                        OutboxStatus.PENDING,
                        LocalDateTime.now(),
                        PageRequest.of(0, batchSize)
                );

        for (OutboxEvent event : events) {
            publishSingleEvent(event.getId());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publishSingleEvent(UUID outboxEventId) {
        OutboxEvent event = outboxEventRepository.findById(outboxEventId)
                .orElse(null);

        if (event == null || event.getStatus() != OutboxStatus.PENDING) {
            return;
        }

        try {
            Object payload = decodePayload(event);
            rabbitTemplate.convertAndSend(event.getExchangeName(), event.getRoutingKey(), payload);

            event.setStatus(OutboxStatus.PUBLISHED);
            event.setPublishedAt(LocalDateTime.now());
            event.setLastError(null);
            outboxEventRepository.save(event);
        } catch (Exception e) {
            int nextAttempt = event.getAttempts() + 1;
            event.setAttempts(nextAttempt);
            event.setLastError(trimError(e.getMessage()));

            if (nextAttempt >= maxAttempts) {
                event.setStatus(OutboxStatus.DEAD);
            } else {
                event.setNextAttemptAt(LocalDateTime.now().plusSeconds((long) retryDelaySeconds * nextAttempt));
            }

            outboxEventRepository.save(event);
            log.warn("Outbox publish failed for event {} attempt {}", event.getId(), nextAttempt, e);
        }
    }

    private Object decodePayload(OutboxEvent event) throws JsonProcessingException {
        if (TEACHER_PROFILE_CREATE_EVENT.equals(event.getEventType())) {
            return objectMapper.readValue(event.getPayload(), TeacherProfileCreateMessage.class);
        }

        return event.getPayload();
    }

    private String trimError(String message) {
        if (message == null) {
            return null;
        }

        int maxLen = 2000;
        if (message.length() <= maxLen) {
            return message;
        }

        return message.substring(0, maxLen);
    }
}

