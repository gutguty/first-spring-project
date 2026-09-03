package ru.gazprom.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private boolean success;
    private String message;
    private BigDecimal amount;
}
