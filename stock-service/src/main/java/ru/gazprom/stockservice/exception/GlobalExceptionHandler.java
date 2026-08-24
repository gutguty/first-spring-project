package ru.gazprom.stockservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StockNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleStockNotFoundException(StockNotFoundException exception) {
        return new ResponseEntity<>(new ExceptionResponse(LocalDateTime.now(), exception.getMessage(), "Stock not found"), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ExceptionResponse> handleForbiddenException(ForbiddenException exception) {
        return new ResponseEntity<>(new ExceptionResponse(LocalDateTime.now(), exception.getMessage(),"It is forbidden"), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidArguments(IllegalArgumentException exception) {
        return new ResponseEntity<>(new ExceptionResponse(LocalDateTime.now(), exception.getMessage(), "Invalid arguments"), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleAll(Exception exception) {
        return new ResponseEntity<>(new ExceptionResponse(LocalDateTime.now(), exception.getMessage(), "Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
