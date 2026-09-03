package ru.gazprom.server.exception;

import org.springframework.http.HttpStatus;

public class ServerInternalException extends ValidationException {
    public ServerInternalException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
