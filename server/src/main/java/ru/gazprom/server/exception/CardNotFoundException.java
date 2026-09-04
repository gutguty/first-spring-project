package ru.gazprom.server.exception;

import org.springframework.http.HttpStatus;

public class CardNotFoundException extends ValidationException {
    public CardNotFoundException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
