package ru.gazprom.server.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.gazprom.server.dto.OrderDTO;

@Service
@Slf4j
@AllArgsConstructor
public class OrderEventProducer {
    private final KafkaTemplate<String, OrderDTO> kafkaTemplate;

    public void sendOrderCreated(OrderDTO orderDTO) {
        kafkaTemplate.send("order-created-topic", orderDTO.getId().toString(), orderDTO);
        log.info("Sent order with id = {}",orderDTO.getId());
    }
}
