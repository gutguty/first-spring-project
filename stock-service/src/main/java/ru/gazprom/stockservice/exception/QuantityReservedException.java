package ru.gazprom.stockservice.exception;

import org.springframework.http.HttpStatus;

public class QuantityReservedException extends ValidationException {
    public QuantityReservedException(Integer quantity, Integer reserved) {
        super("Reserved " + reserved + " is bigger than quantity " + quantity, HttpStatus.BAD_REQUEST);
    }
}
