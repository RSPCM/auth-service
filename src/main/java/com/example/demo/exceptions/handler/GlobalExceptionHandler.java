package com.example.demo.exceptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.exceptions.AlreadyExistsException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.exceptions.OtpEarlyResentException;
import com.example.demo.exceptions.OtpLimitExitedException;
import com.example.demo.exceptions.PhoneNumberNotVerifiedException;
import com.example.demo.exceptions.dto.ErrorResponse;
import com.example.demo.exceptions.entity.ErrorMessages;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OtpLimitExitedException.class)
    public ResponseEntity<?> handleOtpLimitException(OtpLimitExitedException e) {
        log.error("OtpLimitExitedException: {}", e.getMessage());

        ErrorResponse response = new ErrorResponse(
                ErrorMessages.OTP_LIMIT,
                "OTP_LIMIT");
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(response);
    }

    @ExceptionHandler(OtpEarlyResentException.class)
    public ResponseEntity<?> handleOtpEarlyResentException(OtpEarlyResentException e) {
        log.error("OtpEarlyResentException: {}", e.getMessage());

        ErrorResponse response = new ErrorResponse(
                ErrorMessages.OTP_TOO_EARLY,
                "OTP_TOO_EARLY");
        return ResponseEntity.status(HttpStatus.TOO_EARLY).body(response);
    }

    @ExceptionHandler(PhoneNumberNotVerifiedException.class)
    public ResponseEntity<?> handlePhoneNumberNotVerified(PhoneNumberNotVerifiedException e) {
        log.error("PhoneNumberNotVerified: {}", e.getMessage());

        ErrorResponse response = new ErrorResponse(
                ErrorMessages.PHONE_NOT_VERIFIED,
                "PHONE_NOT_VERIFIED");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFound(EntityNotFoundException e) {
        log.error("EntityNotFoundException: {}", e.getMessage());

        ErrorResponse response = new ErrorResponse(
                ErrorMessages.ENTITY_NOT_FOUND,
                "NOT_FOUND");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<?> handleAlreadyExist(AlreadyExistsException e) {
        log.error("AlreadyExistException: {}", e.getMessage());

        ErrorResponse response = new ErrorResponse(
                ErrorMessages.ALREADY_EXIST,
                "ALREADY_EXIST");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnexpected(Exception e) {
        log.error("Unhandled Exception: {}", e.getMessage());

        ErrorResponse response = new ErrorResponse(
                "Internal server error",
                "INTERNAL_ERROR");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
