package ru.gazprom.server.exception;

import org.springframework.http.HttpStatus;

public class KafkaReplyError extends ValidationError{
    public KafkaReplyError(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
