package com.example.authservice.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("Unauthorized error: {}", authException.getMessage());

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String message = authException.getCause() != null ? authException.getCause().getMessage() : authException.getMessage();

        Map<String, Object> responseBody = new HashMap<>();

        responseBody.put("timestamp", Instant.ofEpochMilli(System.currentTimeMillis()).toString());
        responseBody.put("errorCode", "Unauthorized");
        responseBody.put("message", message);
        responseBody.put("userMessage", "You cannot log in, please authenticate.");
        responseBody.put("path", request.getRequestURI());

        response.getOutputStream().write(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsBytes(responseBody));
    }
}
