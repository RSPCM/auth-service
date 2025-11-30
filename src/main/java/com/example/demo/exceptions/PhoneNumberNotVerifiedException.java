package com.example.demo.exceptions;

public class PhoneNumberNotVerifiedException extends RuntimeException {
    static private final String MESSAGE = "Phone number `%s` is not verified";

    public PhoneNumberNotVerifiedException(String phoneNumber) {
        super(String.format(MESSAGE, phoneNumber));
    }
}