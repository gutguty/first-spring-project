package ru.gazprom.stockservice.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenError extends ValidationError {
    public ForbiddenError(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
