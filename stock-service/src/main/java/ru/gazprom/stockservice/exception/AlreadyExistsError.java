package ru.gazprom.stockservice.exception;

import org.springframework.http.HttpStatus;

public class AlreadyExistsError extends ValidationError {
    public AlreadyExistsError(Long cardId) {
        super("Stock with cardId " + cardId + " is already exists", HttpStatus.BAD_REQUEST);
    }
}
