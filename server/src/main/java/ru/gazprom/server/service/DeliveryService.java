package ru.gazprom.server.service;

import org.springframework.stereotype.Service;
import ru.gazprom.server.delivery.DeliveryStrategy;
import ru.gazprom.server.model.Address;
import ru.gazprom.server.model.Card;
import ru.gazprom.server.enums.DeliveryType;

import java.util.List;

@Service
public class DeliveryService {
    private final List<DeliveryStrategy> deliveryStrategies;

    public DeliveryService(List<DeliveryStrategy> deliveryStrategies) {
        this.deliveryStrategies = deliveryStrategies;
    }

    public double calculatePrice(Card card, Address address, DeliveryType deliveryType) {
        for (DeliveryStrategy delivery : deliveryStrategies) {
            if (delivery.getType() == deliveryType) {
                double tempPrice = delivery.calculateDelivery(card, address);
                return Math.round(tempPrice);
            }
        }

        throw new IllegalArgumentException("No delivery strategy for type " + deliveryType);
    }
}
