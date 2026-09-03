package ru.gazprom.server.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<?>> handleAll(Exception exception) {
        ServerInternalException serverInternalException = new ServerInternalException(exception.getMessage());
        Response<?> response = new Response<>(LocalDateTime.now(), "Server internal error " + exception.getMessage(), false,null, List.of(serverInternalException));
        return new ResponseEntity<>(response, serverInternalException.getHttpStatus());
    }
}
