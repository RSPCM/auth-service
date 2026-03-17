package com.example.authservice.log;

import com.example.authservice.log.entity.RequestAuditLog;
import com.example.authservice.otp.OtpRepository;
import com.example.authservice.repository.RequestAuditLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.mockito.Mockito;

import java.util.concurrent.Executor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RequestAuditLogIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RequestAuditLogRepository requestAuditLogRepository;

    @BeforeEach
    void setUp() {
        requestAuditLogRepository.deleteAll();
    }

    @Test
    void shouldAddRequestIdHeaderAndPersistAuditLog() throws Exception {
        var mvcResult = mockMvc.perform(get("/api/v1/auth/login"))
                .andExpect(header().exists("X-Request-Id"))
                .andReturn()
                .getResponse();

        String requestId = mvcResult.getHeader("X-Request-Id");
        int responseStatus = mvcResult.getStatus();

        assertThat(requestId).isNotBlank();
        assertThat(responseStatus).isGreaterThanOrEqualTo(400);

        RequestAuditLog log = requestAuditLogRepository.findAll().stream()
                .filter(item -> requestId.equals(item.getRequestId()))
                .findFirst()
                .orElseThrow();

        assertThat(log.getHttpMethod()).isEqualTo("GET");
        assertThat(log.getRequestUri()).isEqualTo("/api/v1/auth/login");
        assertThat(log.getResponseStatus()).isEqualTo(responseStatus);
        assertThat(log.getDurationMs()).isNotNull().isGreaterThanOrEqualTo(0L);
        assertThat(log.getRemoteAddress()).isNotBlank();
    }

    @TestConfiguration
    static class SyncAsyncConfig {
        @Bean(name = "NotificationTaskExecutor")
        Executor notificationTaskExecutor() {
            return new SyncTaskExecutor();
        }

        @Bean
        OtpRepository otpRepository() {
            return Mockito.mock(OtpRepository.class);
        }
    }
}

