package ru.gazprom.server.delivery;

import org.springframework.stereotype.Component;
import ru.gazprom.server.enums.CityZone;
import ru.gazprom.server.model.Address;
import ru.gazprom.server.model.Card;
import ru.gazprom.server.enums.DeliveryType;

@Component
public class CourierDeliveryStrategy implements DeliveryStrategy {
    private static final double CITY_CENTER_COEFFICIENT = 1.15;
    private static final double CITY_SUBURB_COEFFICIENT = 1.25;
    private static final double CITY_COUNTRYSIDE_COEFFICIENT = 1.45;
    private static final double COURIER_FEE = 500.0;


    @Override
    public DeliveryType getType() {
        return DeliveryType.COURIER;
    }

    @Override
    public double calculateDelivery(Card card, Address address) {
        double coefficientCityZone = calculateCoefficient(address.getCityZone());

        return (card.getPrice() + COURIER_FEE) * coefficientCityZone;
    }

    public double calculateCoefficient(CityZone cityZone) {
        return switch (cityZone) {
            case CENTER -> CITY_CENTER_COEFFICIENT;
            case SUBURB -> CITY_SUBURB_COEFFICIENT;
            case COUNTRYSIDE -> CITY_COUNTRYSIDE_COEFFICIENT;
            default -> throw new IllegalArgumentException("Unknown city zone " + cityZone);
        };
    }
}
