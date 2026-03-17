package com.example.authservice.log;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuditLoggingFilter extends OncePerRequestFilter {
    private final RequestAuditLogService requestAuditLogService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(request, response);
        } finally {
            try {
                long durationMs = System.currentTimeMillis() - startTime;

                String requestId = RequestContext.getRequestId();
                String method = request.getMethod();
                String uri = request.getRequestURI();
                String query = request.getQueryString();
                String userAgent = request.getHeader("User-Agent");
                int status = response.getStatus();
                String remoteAddress = resolveClientIp(request);

                requestAuditLogService.log(
                        requestId, method, uri, query, remoteAddress, userAgent, status, durationMs
                );
            }catch (Exception ex) {
                log.warn("Failed to persist audit log: {}", ex.getMessage());
                log.debug("Audit logging failure", ex);
            }
        }
    }

    private String resolveClientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) return xff.split(",")[0].trim();
        return request.getRemoteAddr();
    }
}
