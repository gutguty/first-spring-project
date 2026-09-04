package ru.gazprom.server.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.gazprom.server.dto.OrderDTO;
import ru.gazprom.server.enums.DeliveryType;
import ru.gazprom.server.enums.PaymentType;
import ru.gazprom.server.exception.AddressNotFoundException;
import ru.gazprom.server.exception.CardNotFoundException;
import ru.gazprom.server.exception.Response;
import ru.gazprom.server.model.Address;
import ru.gazprom.server.model.Card;
import ru.gazprom.server.repository.AddressRepository;
import ru.gazprom.server.repository.CardRepository;
import ru.gazprom.server.service.OrderService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/order")
    public Response<OrderDTO> order(@RequestParam Long cardId, @RequestParam Long addressId, @RequestParam DeliveryType deliveryType, @RequestParam PaymentType paymentType) {
      return orderService.order(cardId, addressId, deliveryType, paymentType);
    }
}
