package ru.gazprom.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CardNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleCardNotFoundException(CardNotFoundException exception) {
        return new ResponseEntity<>(new ExceptionResponse(LocalDateTime.now(), exception.getMessage(), "Card not found"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleCategoryNotFoundException(CategoryNotFoundException exception) {
        return new ResponseEntity<>(new ExceptionResponse(LocalDateTime.now(), exception.getMessage(), "Category not found"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidArguments(IllegalArgumentException exception) {
        return new ResponseEntity<>(new ExceptionResponse(LocalDateTime.now(), exception.getMessage(), "Invalid arguments"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleAll(Exception exception) {
        return new ResponseEntity<>(new ExceptionResponse(LocalDateTime.now(), exception.getMessage(), "Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidStockResponseException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidStockResponseException(InvalidStockResponseException exception) {
        return new ResponseEntity<>(new ExceptionResponse(LocalDateTime.now(), exception.getMessage(), "Stock response is incorrect"), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(WebCustomClientException.class)
    public ResponseEntity<ExceptionResponse> handleWebClientException(WebCustomClientException exception) {
        return new ResponseEntity<>(new ExceptionResponse(LocalDateTime.now(), exception.getMessage(), "Service is failed"), exception.getHttpStatus());
    }

    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleAddressNotFoundException(AddressNotFoundException exception) {
        return new ResponseEntity<>(new ExceptionResponse(LocalDateTime.now(), exception.getMessage(), "Address is not found"), HttpStatus.NOT_FOUND);
    }
}
