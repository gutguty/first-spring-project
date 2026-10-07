package ru.gazprom.stockservice.exception;

import org.springframework.http.HttpStatus;

public class CardIdNotFound extends ValidationError {
    public CardIdNotFound(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
