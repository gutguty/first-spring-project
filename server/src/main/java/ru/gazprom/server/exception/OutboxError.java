package ru.gazprom.server.exception;

import org.springframework.http.HttpStatus;

public class OutboxError extends ValidationError{
    public OutboxError(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
