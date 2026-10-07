package ru.gazprom.stockservice.exception;

import org.springframework.http.HttpStatus;

public class FieldRequiredError extends ValidationError {
    public FieldRequiredError(String fieldName) {
        super(fieldName + " is null", HttpStatus.BAD_REQUEST);
    }
}
