package com.example.authservice.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST) // 400
    public ResponseEntity<?> badRequestException(BadRequestException e, HttpServletRequest request) {
        log.error("BadRequestException: {}", e.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(
                new ErrorMessage(new Timestamp(System.currentTimeMillis()), ErrorCodes.BadRequest.getName(), e.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(UNAUTHORIZED) // 401
    public ResponseEntity<?> unauthorizedException(AuthenticationException e, HttpServletRequest request) {
        log.error("AuthenticationException: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorMessage(new Timestamp(System.currentTimeMillis()), ErrorCodes.Unauthorized.getName(), e.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(FORBIDDEN) // 403
    public ResponseEntity<?> accessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        log.error("AccessDeniedException: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorMessage(new Timestamp(System.currentTimeMillis()), ErrorCodes.Unauthorized.getName(), e.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(NOT_FOUND) // 404
    public ResponseEntity<?> noResourceFoundException(HttpServletRequest request) {
        log.error("NoResourceFoundException: {}", request.getRequestURI());
        return ResponseEntity.status(NOT_FOUND).body(
                new ErrorMessage(new Timestamp(System.currentTimeMillis()), ErrorCodes.NotFound.getName(), "Not Found", request.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR) // 500
    public ResponseEntity<ErrorMessage> handleGenericException(Exception ex, HttpServletRequest request) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ErrorMessage
                (new Timestamp(System.currentTimeMillis()), ErrorCodes.InternalServerError.name(), ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(UNAUTHORIZED) //401
    public ResponseEntity<ErrorMessage> handleBadCredentialsException(HttpServletRequest request) {
        return ResponseEntity.status(UNAUTHORIZED).body(new ErrorMessage(
                new Timestamp(System.currentTimeMillis()), ErrorCodes.Unauthorized.getName(), "Bad credentials exception", request.getRequestURI()));
    }

    @ExceptionHandler
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                {
                    String field = error.getField();
                    String message = error.getDefaultMessage();
                    errors.put(field, message);
                }
        );
        return ResponseEntity.status(BAD_REQUEST).body(errors);
    }
}
