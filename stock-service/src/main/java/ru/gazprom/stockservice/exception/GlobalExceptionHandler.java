package ru.gazprom.stockservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static ru.gazprom.stockservice.utils.ResponseUtils.responseError;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<?>> handleAll(Exception exception) {
        ServerInternalError serverError = new ServerInternalError(exception.getMessage());
        Response<?> response = responseError("Server internal error " + exception.getMessage(), serverError);
        return new ResponseEntity<>(response, serverError.getHttpStatus());
    }
}
