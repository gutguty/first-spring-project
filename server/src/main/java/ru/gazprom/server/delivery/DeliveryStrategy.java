package ru.gazprom.server.delivery;

import ru.gazprom.server.model.Address;
import ru.gazprom.server.model.Card;
import ru.gazprom.server.enums.DeliveryType;

import java.math.BigDecimal;

public interface DeliveryStrategy {
    DeliveryType getType();
    BigDecimal calculateDelivery(Card card, Address address);
}
