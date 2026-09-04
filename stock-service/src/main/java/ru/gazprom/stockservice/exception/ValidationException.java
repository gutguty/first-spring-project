package ru.gazprom.stockservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ValidationException {
    private final String message;
    private final HttpStatus httpStatus;

    public ValidationException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

}
