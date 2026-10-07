package ru.gazprom.server.exception;

import org.springframework.http.HttpStatus;

public class ServerInternalError extends ValidationError {
    public ServerInternalError(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
