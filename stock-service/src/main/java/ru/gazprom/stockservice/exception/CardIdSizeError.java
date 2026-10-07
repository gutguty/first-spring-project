package ru.gazprom.stockservice.exception;

import org.springframework.http.HttpStatus;

public class CardIdSizeError extends ValidationError{
    public CardIdSizeError(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
