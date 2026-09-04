package ru.gazprom.server.utils;

import ru.gazprom.server.exception.Response;
import ru.gazprom.server.exception.ValidationError;

import java.time.LocalDateTime;
import java.util.List;

public class ResponseUtils {
    public static <T> Response<T> responseSuccess(String methodName, T data) {
        return new Response<>(LocalDateTime.now(), methodName, true, data, List.of());
    }

    public static <T> Response<T> responseError(String methodName, ValidationError error) {
        return new Response<>(LocalDateTime.now(), methodName, false, null, List.of(error));
    }

    public static <T> Response<T> responseError(String methodName, List<ValidationError> error) {
        return new Response<>(LocalDateTime.now(), methodName, false, null, error);
    }
}
