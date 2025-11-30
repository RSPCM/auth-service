package com.example.demo.exceptions;

public class EntityNotFoundException extends RuntimeException {
    private static final String MESSAGE = "%s entity with id `%s` not found";

    public EntityNotFoundException(String entityName, String id) {
        super(String.format(MESSAGE, entityName, id));
    }
}
