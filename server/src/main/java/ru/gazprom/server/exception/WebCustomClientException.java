package ru.gazprom.server.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class WebCustomClientException extends RuntimeException {

    private final HttpStatusCode httpStatus;

    public WebCustomClientException(String message, HttpStatusCode httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
