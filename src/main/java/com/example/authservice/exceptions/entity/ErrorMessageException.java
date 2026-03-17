package com.example.authservice.exceptions.entity;

import com.example.authservice.exceptions.ErrorCodes;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorMessageException extends RuntimeException{

    private final String message;
    private final ErrorCodes errorCode;

    public ErrorMessageException(String message, ErrorCodes errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }
}
