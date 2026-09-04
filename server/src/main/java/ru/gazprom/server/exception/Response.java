package ru.gazprom.server.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {
    private LocalDateTime localDateTime;
    private String methodName;
    private boolean success;
    private T data;
    private List<ValidationError> listErrors;
}
