package ru.gazprom.server.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public class WebCustomClientException extends ValidationException {

    public WebCustomClientException(String message, HttpStatus httpStatus) {
        super("Error in web client " + message, httpStatus);
    }

}
