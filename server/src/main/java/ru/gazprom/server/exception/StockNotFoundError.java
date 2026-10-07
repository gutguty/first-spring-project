package ru.gazprom.server.exception;

import org.springframework.http.HttpStatus;

public class StockNotFoundError extends ValidationError {
    public StockNotFoundError(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}