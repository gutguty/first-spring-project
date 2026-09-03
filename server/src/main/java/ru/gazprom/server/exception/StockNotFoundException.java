package ru.gazprom.server.exception;

import org.springframework.http.HttpStatus;

public class StockNotFoundException extends ValidationException {
    public StockNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}