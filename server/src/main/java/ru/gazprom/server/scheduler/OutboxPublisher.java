package ru.gazprom.server.scheduler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.gazprom.server.model.OutboxEvent;
import ru.gazprom.server.service.OutboxEventService;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class OutboxPublisher {
    private final OutboxEventService outboxEventService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 5000)
    @Transactional
     public void publish() {
        List<OutboxEvent> outboxEventList = outboxEventService.findEventsToPublish();

        for (OutboxEvent event: outboxEventList) {
            try {
                kafkaTemplate.send(event.getTopic(), event.getMessageKey(), event.getPayload()).get();
                outboxEventService.markSent(event);
                log.info("Published outbox event id={}, eventId={}, topic={}",
                        event.getId(), event.getEventId(), event.getTopic());
            } catch (Exception e) {
                outboxEventService.markFailed(event);
                log.error("Failed to publish outbox event id={}, retry={}",
                        event.getId(), event.getRetryCount() + 1, e);
            }
        }
    }
}
