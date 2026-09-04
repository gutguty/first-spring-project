package ru.gazprom.server.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

import static ru.gazprom.server.utils.ResponseUtils.responseError;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<?>> handleAll(Exception exception) {
        log.error("EXCEPTION ",exception);
        ServerInternalError serverInternalError = new ServerInternalError(exception.getMessage());
        Response<?> response = responseError("Server internal error " + exception.getMessage(), serverInternalError);
        return new ResponseEntity<>(response, serverInternalError.getHttpStatus());
    }
}
