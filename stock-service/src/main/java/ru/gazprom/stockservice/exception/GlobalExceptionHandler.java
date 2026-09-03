package ru.gazprom.stockservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.gazprom.stockservice.model.Stock;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<?>> handleAll(Exception exception) {
        ServerInternalException serverError = new ServerInternalException(exception.getMessage());
        Response<?> response = new Response<>(LocalDateTime.now(), exception.getMessage(), false, null, List.of(serverError));
        return new ResponseEntity<>(response, serverError.getHttpStatus());
    }
}
