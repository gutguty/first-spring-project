package ru.gazprom.stockservice.exception;

import org.springframework.http.HttpStatus;

public class StockNotFoundError extends ValidationError {
    public StockNotFoundError(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
