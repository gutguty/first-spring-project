package ru.gazprom.server.exception;

import org.springframework.http.HttpStatus;

public class FieldRequiredException extends ValidationException {
    public FieldRequiredException(String fieldName) {
        super(fieldName + " is null", HttpStatus.BAD_REQUEST);
    }
}
