package ru.gazprom.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.gazprom.server.dto.KafkaStockReply;
import ru.gazprom.server.dto.OrderDTO;
import ru.gazprom.server.exception.Response;
import ru.gazprom.server.kafka.OrderEventProducer;
import ru.gazprom.server.kafka.StockKafkaClient;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/kafka")
@RequiredArgsConstructor
public class KafkaController {

    private final OrderEventProducer orderEventProducer;
    private final StockKafkaClient stockKafkaClient;

    @PostMapping("/orders")
    public String sendOrderCreated(@RequestParam Long id) {
        OrderDTO order = new OrderDTO(id, BigDecimal.TEN, null);
        orderEventProducer.sendOrderCreated(order);
        return "Order event sent, id=" + id;
    }

    @GetMapping("/stock")
    public Response<KafkaStockReply> checkStock(@RequestParam Long cardId) {
        return stockKafkaClient.requestStock(cardId);
    }
}