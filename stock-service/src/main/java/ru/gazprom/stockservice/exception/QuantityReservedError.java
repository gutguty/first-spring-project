package ru.gazprom.stockservice.exception;

import org.springframework.http.HttpStatus;

public class QuantityReservedError extends ValidationError {
    public QuantityReservedError(Integer quantity, Integer reserved) {
        super("Reserved " + reserved + " is bigger than quantity " + quantity, HttpStatus.BAD_REQUEST);
    }
}
