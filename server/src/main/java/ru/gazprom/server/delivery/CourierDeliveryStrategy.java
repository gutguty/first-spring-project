package ru.gazprom.server.delivery;

import org.springframework.stereotype.Component;
import ru.gazprom.server.enums.CityZone;
import ru.gazprom.server.model.Address;
import ru.gazprom.server.model.Card;
import ru.gazprom.server.enums.DeliveryType;

import java.math.BigDecimal;

@Component
public class CourierDeliveryStrategy implements DeliveryStrategy {
    private static final BigDecimal CITY_CENTER_COEFFICIENT = new BigDecimal("1.15");
    private static final BigDecimal CITY_SUBURB_COEFFICIENT = new BigDecimal("1.25");
    private static final BigDecimal CITY_COUNTRYSIDE_COEFFICIENT = new BigDecimal("1.45");
    private static final BigDecimal COURIER_FEE = new BigDecimal("500.0");


    @Override
    public DeliveryType getType() {
        return DeliveryType.COURIER;
    }

    @Override
    public BigDecimal calculateDelivery(Card card, Address address) {
        BigDecimal coefficientCityZone = calculateCoefficient(address.getCityZone());

        return card.getPrice().add(COURIER_FEE).multiply(coefficientCityZone);
    }

    public BigDecimal calculateCoefficient(CityZone cityZone) {
        return switch (cityZone) {
            case CENTER -> CITY_CENTER_COEFFICIENT;
            case SUBURB -> CITY_SUBURB_COEFFICIENT;
            case COUNTRYSIDE -> CITY_COUNTRYSIDE_COEFFICIENT;
            default -> throw new IllegalArgumentException("Unknown city zone " + cityZone);
        };
    }
}
