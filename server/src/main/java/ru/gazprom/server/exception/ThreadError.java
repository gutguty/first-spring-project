package ru.gazprom.server.exception;

import org.springframework.http.HttpStatus;

public class ThreadError extends ValidationError {
    public ThreadError(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
