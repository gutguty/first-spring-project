package ru.gazprom.stockservice.exception;

import org.springframework.http.HttpStatus;

public class NegativeValueError extends ValidationError {
    public NegativeValueError(String fieldName, Integer value) {
        super(fieldName + " is negative: " + value, HttpStatus.BAD_REQUEST);
    }
}