package ru.gazprom.server.delivery;

import org.springframework.stereotype.Component;
import ru.gazprom.server.enums.DeliveryType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DeliveryFactory {
    private final Map<DeliveryType, DeliveryStrategy> strategies;

    public DeliveryFactory(List<DeliveryStrategy> strategies) {
        this.strategies = strategies.stream().collect(Collectors.toMap(DeliveryStrategy::getType, strategy -> strategy));
    }

    public DeliveryStrategy getStrategy(DeliveryType deliveryType) {
        DeliveryStrategy strategy = strategies.get(deliveryType);
        if (strategy == null) {
            throw  new IllegalArgumentException("Delivery type " + deliveryType + " is not found");
        }

        return strategy;
    }
}
