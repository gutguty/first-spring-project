package ru.gazprom.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.gazprom.server.exception.JsonSerializationError;
import ru.gazprom.server.exception.Response;
import ru.gazprom.server.model.OutboxEvent;
import ru.gazprom.server.repository.OutboxEventRepository;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static ru.gazprom.server.utils.ResponseUtils.responseError;
import static ru.gazprom.server.utils.ResponseUtils.responseSuccess;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxEventService {
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    public Response<Void> saveEvent(String topic, String messageKey, Object payloadObject) {
        String payload;
        try {
            payload = objectMapper.writeValueAsString(payloadObject);
        } catch (JacksonException e) {
            log.error("Failed to serialize outbox payload for topic = {}", topic, e);
            return responseError("saveEvent", new JsonSerializationError("Failed to serialize outbox payload " + e.getMessage()));
        }

        LocalDateTime timeNow = LocalDateTime.now();
        OutboxEvent event = new OutboxEvent(
                null,
                UUID.randomUUID().toString(),
                topic,
                messageKey,
                payload,
                "NEW",
                0,
                timeNow,
                timeNow,
                null
        );

        outboxEventRepository.save(event);
        log.info("Outbox event saved eventId = {}, topic = {}", event.getEventId(), topic);

        return responseSuccess("saveEvent", null);
    }

    public List<OutboxEvent> findEventsToPublish() {
        return outboxEventRepository.findReadyToPublish(List.of("NEW", "FAILED"), LocalDateTime.now());
    }

    public void markSent(OutboxEvent event) {
        event.setStatus("SENT");
        event.setProcessedAt(LocalDateTime.now());
        outboxEventRepository.save(event);
    }

    public void markFailed(OutboxEvent event) {
        event.setRetryCount(event.getRetryCount() + 1);
        event.setStatus("FAILED");
        event.setAvailableAt(LocalDateTime.now().plusSeconds(15));
        outboxEventRepository.save(event);
    }
}
