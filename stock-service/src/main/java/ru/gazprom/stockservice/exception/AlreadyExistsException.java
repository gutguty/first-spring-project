package ru.gazprom.stockservice.exception;

import org.springframework.http.HttpStatus;

public class AlreadyExistsException extends ValidationException{
    public AlreadyExistsException(Long cardId) {
        super("Stock with cardId " + cardId + " is already exists", HttpStatus.BAD_REQUEST);
    }
}
