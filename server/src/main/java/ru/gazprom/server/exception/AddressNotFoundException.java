package ru.gazprom.server.exception;

import org.springframework.http.HttpStatus;

public class AddressNotFoundException extends ValidationException{
    public AddressNotFoundException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
