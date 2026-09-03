package ru.gazprom.server.delivery;

import org.springframework.stereotype.Component;
import ru.gazprom.server.model.Address;
import ru.gazprom.server.model.Card;
import ru.gazprom.server.enums.DeliveryType;

import java.math.BigDecimal;

@Component
public class PickUpDeliveryStrategy implements DeliveryStrategy {
    @Override
    public DeliveryType getType() {
        return DeliveryType.PICK_UP;
    }

    @Override
    public BigDecimal calculateDelivery(Card card, Address address) {
        return card.getPrice();
    }
}
