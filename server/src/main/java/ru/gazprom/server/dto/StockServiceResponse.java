package ru.gazprom.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockServiceResponse {
    private LocalDateTime time;
    private String methodName;
    private boolean success;
    private StockDTO stock;
    private List<StockErrorDTO> listErrors;
}