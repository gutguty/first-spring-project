package ru.gazprom.server.exception;

import org.springframework.http.HttpStatus;

public class CategoryNotFoundError extends ValidationError {
    public CategoryNotFoundError(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
