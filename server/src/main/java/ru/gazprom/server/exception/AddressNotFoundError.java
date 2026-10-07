package ru.gazprom.server.exception;

import org.springframework.http.HttpStatus;

public class AddressNotFoundError extends ValidationError {
    public AddressNotFoundError(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
