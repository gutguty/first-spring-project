package ru.gazprom.server.payment;

import org.springframework.stereotype.Component;
import ru.gazprom.server.dto.PaymentDTO;
import ru.gazprom.server.enums.PaymentType;

import java.math.BigDecimal;

@Component
public class PartnerCardPaymentStrategy implements PaymentStrategy {
    private final static BigDecimal PARTNER_DISCOUNT = new BigDecimal("0.9");

    @Override
    public PaymentDTO paymentProcess(BigDecimal amount) {
        BigDecimal amountWithDiscount = amount.multiply(PARTNER_DISCOUNT);
        return new PaymentDTO(true, "Payment partner card", amountWithDiscount);
    }

    @Override
    public PaymentType getType() {
        return PaymentType.PARTNER_CARD;
    }
}
