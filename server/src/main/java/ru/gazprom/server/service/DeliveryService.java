package ru.gazprom.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gazprom.server.delivery.DeliveryFactory;
import ru.gazprom.server.delivery.DeliveryStrategy;
import ru.gazprom.server.exception.AddressNotFoundError;
import ru.gazprom.server.exception.CardNotFoundError;
import ru.gazprom.server.exception.Response;
import ru.gazprom.server.model.Card;
import ru.gazprom.server.enums.DeliveryType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static ru.gazprom.server.utils.ResponseUtils.responseError;
import static ru.gazprom.server.utils.ResponseUtils.responseSuccess;


@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final CardService cardService;
    private final AddressService addressService;
    private final DeliveryFactory deliveryFactory;

    public Response<BigDecimal> calculatePrice(Long cardId, Long addressId, DeliveryType deliveryType) {
        return cardService.findCardById(cardId)
                .map(card -> calculateDeliveryStrategyByAddressId(card, addressId, deliveryType))
                .orElseGet(() -> responseError("calculatePrice", new CardNotFoundError("Card with id " + cardId + " is not found")));
    }

    public Response<BigDecimal> calculatePrice(Card card, Long addressId, DeliveryType deliveryType) {
        return calculateDeliveryStrategyByAddressId(card, addressId, deliveryType);
    }

    private Response<BigDecimal> calculateDeliveryStrategyByAddressId(Card card, Long addressId, DeliveryType deliveryType) {
        return addressService.findAddressById(addressId)
                .map(address -> {
                    DeliveryStrategy deliveryStrategy = deliveryFactory.getStrategy(deliveryType);
                    BigDecimal calculateDelivery = deliveryStrategy.calculateDelivery(card, address);
                    return responseSuccess("calculatePrice", calculateDelivery);
                })
                .orElseGet(() -> responseError("calculatePrice", new AddressNotFoundError("Address with id " + addressId + " is not found")));
    }
}
