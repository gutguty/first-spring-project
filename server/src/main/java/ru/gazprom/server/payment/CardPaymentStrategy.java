package ru.gazprom.server.payment;

import org.springframework.stereotype.Component;
import ru.gazprom.server.dto.PaymentDTO;
import ru.gazprom.server.enums.PaymentType;

import java.math.BigDecimal;

@Component
public class CardPaymentStrategy implements PaymentStrategy {
    @Override
    public PaymentDTO paymentProcess(BigDecimal amount) {
        return new PaymentDTO(true, "Payment card", amount);
    }

    @Override
    public PaymentType getType() {
        return PaymentType.CARD;
    }
}
