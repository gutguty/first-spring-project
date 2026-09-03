package ru.gazprom.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockErrorDTO {
    private String message;
    private String httpStatus;
}