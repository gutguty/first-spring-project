package ru.gazprom.server.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class WebCustomClientError extends ValidationError {

    public WebCustomClientError(String message, HttpStatus httpStatus) {
        super("Error in web client " + message, httpStatus);
    }

}
