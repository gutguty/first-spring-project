package ru.gazprom.server.payment;

import ru.gazprom.server.dto.PaymentDTO;
import ru.gazprom.server.enums.PaymentType;

import java.math.BigDecimal;

public interface PaymentStrategy {
    PaymentDTO paymentProcess(BigDecimal amount);
    PaymentType getType();
}