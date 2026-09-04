package ru.gazprom.server.exception;

import org.springframework.http.HttpStatus;

public class CategoryNotFoundException extends ValidationException {
    public CategoryNotFoundException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
