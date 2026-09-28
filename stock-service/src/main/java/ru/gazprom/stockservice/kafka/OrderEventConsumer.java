package ru.gazprom.stockservice.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import ru.gazprom.stockservice.dto.OrderDTO;

@Component
@Slf4j
public class OrderEventConsumer {

    @KafkaListener(
        topics = "order-created-topic",
        groupId = "stock-service-group_id",
        containerFactory = "stockKafkaListenerContainerFactory"
    )

    public void consumeOrderCreated(OrderDTO orderDTO, Acknowledgment acknowledgment) {
        log.info("Recieve order = {}", orderDTO.getId());


        acknowledgment.acknowledge();
        log.info("Recieve order success");
    }
}
