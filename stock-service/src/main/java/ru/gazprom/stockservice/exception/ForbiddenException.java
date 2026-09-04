package ru.gazprom.stockservice.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends ValidationException {
    public ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
