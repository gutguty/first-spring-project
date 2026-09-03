package ru.gazprom.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gazprom.server.delivery.DeliveryFactory;
import ru.gazprom.server.delivery.DeliveryStrategy;
import ru.gazprom.server.exception.AddressNotFoundException;
import ru.gazprom.server.exception.CardNotFoundException;
import ru.gazprom.server.exception.Response;
import ru.gazprom.server.model.Address;
import ru.gazprom.server.model.Card;
import ru.gazprom.server.enums.DeliveryType;
import ru.gazprom.server.repository.AddressRepository;
import ru.gazprom.server.repository.CardRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final CardRepository cardRepository;
    private final AddressRepository addressRepository;
    private final DeliveryFactory deliveryFactory;

    public Response<BigDecimal> calculatePrice(Long cardId, Long addressId, DeliveryType deliveryType) {
        return cardRepository.findById(cardId)
                .map(card -> addressRepository.findById(addressId)
                        .map(address -> {
                            DeliveryStrategy deliveryStrategy = deliveryFactory.getStrategy(deliveryType);
                            BigDecimal calculateDelivery = deliveryStrategy.calculateDelivery(card, address);
                            return new Response<>(LocalDateTime.now(), "calculatePrice", true, calculateDelivery, List.of());
                        })
                        .orElseGet(() -> new Response<>(LocalDateTime.now(), "calculatePrice", false, null,
                                List.of(new AddressNotFoundException("Address with id " + addressId + " is not found"))))
                )
                .orElseGet(() -> new Response<>(LocalDateTime.now(), "calculatePrice", false, null,
                        List.of(new CardNotFoundException("Card with id " + cardId + " is not found"))));
    }
}
