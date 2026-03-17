package com.example.authservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCodes {
    InvalidParams(400, "INVALID_PARAM"),
    BadRequest(400, "BAD_REQUEST"),
    Unauthorized(401, "UNAUTHORIZED"),
    Forbidden(403, "FORBIDDEN"),
    InvalidDevice(404, "INVALID_DEVICE"),
    NotFound(404, "NOT_FOUND"),
    InternalServerError(500, "INTERNAL_SERVER_ERROR"),
    ServiceUnavailable(503, "SERVICE_UNAVAILABLE"),
    AlreadyExists(409, "ALREADY_EXISTS"),
    TooManyRequests(429, "TOO_MANY_REQUESTS"),
    InvalidCredentials(401, "INVALID_CREDENTIALS"),
    PhoneNotVerified(403, "PHONE_NOT_VERIFIED");

    private final int statusCode;
    private final String name;
}
