package ru.gazprom.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.gazprom.server.dto.DeliveryPriceRequest;
import ru.gazprom.server.exception.Response;
import ru.gazprom.server.service.DeliveryService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping("/delivery/price")
    public Response<BigDecimal> calculatePriceDelivery(@RequestBody DeliveryPriceRequest request) {
        return deliveryService.calculatePrice(request);
    }
}