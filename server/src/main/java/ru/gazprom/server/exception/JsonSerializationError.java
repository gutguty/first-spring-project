package ru.gazprom.server.exception;


import org.springframework.http.HttpStatus;

public class JsonSerializationError extends ValidationError {
    public JsonSerializationError(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
