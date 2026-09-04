package ru.gazprom.stockservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {
    private LocalDateTime time;
    private String methodName;
    private boolean success;
    private T stock;
    private List<ValidationError> listErrors;
}