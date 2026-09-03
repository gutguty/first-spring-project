package ru.gazprom.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.gazprom.server.exception.Response;
import ru.gazprom.server.enums.DeliveryType;
import ru.gazprom.server.service.DeliveryService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping("/delivery/price")
    public Response<BigDecimal> calculatePriceDelivery(@RequestParam Long cardId, @RequestParam Long addressId, @RequestParam DeliveryType deliveryType) {
        return deliveryService.calculatePrice(cardId, addressId, deliveryType);
    }
}