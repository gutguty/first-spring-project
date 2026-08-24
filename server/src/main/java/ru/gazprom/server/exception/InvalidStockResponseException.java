package ru.gazprom.server.exception;

public class InvalidStockResponseException extends RuntimeException {
    public InvalidStockResponseException(String message) {
        super(message);
    }
}
