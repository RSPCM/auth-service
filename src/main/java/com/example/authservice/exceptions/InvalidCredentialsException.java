package com.example.authservice.exceptions;

public class InvalidCredentialsException extends RuntimeException {
    private static final String MESSAGE = "Invalid credentials: username: `%s`, password: `%s`";

    public InvalidCredentialsException(String username, String password) {
        super(MESSAGE.formatted(username, password));
    }
}
