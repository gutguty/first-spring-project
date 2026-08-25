package ru.gazprom.server.delivery;

import ru.gazprom.server.model.Address;
import ru.gazprom.server.model.Card;
import ru.gazprom.server.enums.DeliveryType;

public interface DeliveryStrategy {
    DeliveryType getType();
    double calculateDelivery(Card card, Address address);
}
