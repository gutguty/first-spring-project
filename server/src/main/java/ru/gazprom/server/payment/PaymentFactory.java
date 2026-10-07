package ru.gazprom.server.payment;

import org.springframework.stereotype.Component;
import ru.gazprom.server.enums.PaymentType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class PaymentFactory {
    private final Map<PaymentType, PaymentStrategy> strategies;

    public PaymentFactory(List<PaymentStrategy> strategies) {
        this.strategies = strategies.stream().collect(Collectors.toMap(PaymentStrategy::getType, strategy -> strategy));
    }

    public PaymentStrategy getStrategy(PaymentType paymentType) {
        PaymentStrategy strategy = strategies.get(paymentType);
        if (strategy == null) {
            throw new IllegalArgumentException("paymentType + " + paymentType + " is not found");
        }

        return strategy;
    }
}