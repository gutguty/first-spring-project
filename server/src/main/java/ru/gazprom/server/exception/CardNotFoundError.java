package ru.gazprom.server.exception;

import org.springframework.http.HttpStatus;

public class CardNotFoundError extends ValidationError {
    public CardNotFoundError(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
