package ru.gazprom.stockservice.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import ru.gazprom.stockservice.dto.KafkaStockReply;
import ru.gazprom.stockservice.dto.KafkaStockRequest;

@Component
@Slf4j
public class StockRequestListener {

    @KafkaListener(
            topics = "stock-request-topic",
            groupId = "stock-service-request-group",
            containerFactory = "stockRequestKafkaListenerContainerFactory"
    )
    @SendTo
    public KafkaStockReply handleStockRequest(KafkaStockRequest request) {
        log.info("Request cardId = {}", request.getCardId());
        boolean available = true;
        return new KafkaStockReply(available, "OK");
    }
}
