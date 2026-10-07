package ru.gazprom.server.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.gazprom.server.dto.OrderDTO;
import ru.gazprom.server.dto.OrderRequest;
import ru.gazprom.server.exception.Response;
import ru.gazprom.server.service.OrderService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/order")
    public Response<OrderDTO> order(@RequestBody OrderRequest request) {
      return orderService.order(request.getCardId(), request.getAddressId(), request.getDeliveryType(), request.getPaymentType());
    }
}
