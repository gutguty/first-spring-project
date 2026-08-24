package ru.gazprom.stockservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ExceptionResponse {
    private LocalDateTime time;
    private String message;
    private String details;
}
