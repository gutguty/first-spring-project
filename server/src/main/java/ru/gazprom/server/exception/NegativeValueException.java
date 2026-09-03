package ru.gazprom.server.exception;

import org.springframework.http.HttpStatus;

public class NegativeValueException extends ValidationException {
    public NegativeValueException(String fieldName, Integer value) {
        super(fieldName + " is negative: " + value, HttpStatus.BAD_REQUEST);
    }
}