package ru.gazprom.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.gazprom.server.exception.AddressNotFoundException;
import ru.gazprom.server.exception.CardNotFoundException;
import ru.gazprom.server.model.Address;
import ru.gazprom.server.model.Card;
import ru.gazprom.server.enums.DeliveryType;
import ru.gazprom.server.repository.AddressRepository;
import ru.gazprom.server.repository.CardRepository;
import ru.gazprom.server.service.DeliveryService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DeliveryController {

    private final CardRepository cardRepository;
    private final DeliveryService deliveryService;
    private final AddressRepository addressRepository;

    @GetMapping("/delivery/price")
    public double calculatePriceDelivery(@RequestParam Long cardId, @RequestParam Long addressId, @RequestParam DeliveryType deliveryType) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new CardNotFoundException("Card with id "+ cardId + " is not found"));
        Address address = addressRepository.findById(addressId).orElseThrow(() -> new AddressNotFoundException("Address with id " + addressId + " is not found"));
        return deliveryService.calculatePrice(card, address, deliveryType);
    }
}