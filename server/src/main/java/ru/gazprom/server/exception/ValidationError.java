package ru.gazprom.server.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ValidationError {
    private final String message;
    private final HttpStatus httpStatus;

    public ValidationError(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
