package ru.gazprom.stockservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.gazprom.stockservice.model.Stock;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class Response<T> {
    private LocalDateTime time;
    private String methodName;
    private boolean success;
    private T stock;
    private List<ValidationException> listErrors;
}