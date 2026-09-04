package ru.gazprom.stockservice.exception;

import org.springframework.http.HttpStatus;

public class StockNotFoundException extends ValidationException {
    public StockNotFoundException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
