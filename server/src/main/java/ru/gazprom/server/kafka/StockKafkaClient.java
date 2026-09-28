package ru.gazprom.server.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Service;
import ru.gazprom.server.dto.KafkaStockReply;
import ru.gazprom.server.dto.KafkaStockRequest;
import ru.gazprom.server.exception.KafkaReplyError;
import ru.gazprom.server.exception.Response;

import java.util.concurrent.TimeUnit;

import static ru.gazprom.server.utils.ResponseUtils.responseError;
import static ru.gazprom.server.utils.ResponseUtils.responseSuccess;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockKafkaClient {
    private final ReplyingKafkaTemplate<String, KafkaStockRequest, KafkaStockReply> replyingKafkaTemplate;

    public Response<KafkaStockReply> requestStock(Long cardId) {
        KafkaStockRequest request = new KafkaStockRequest(cardId);

        ProducerRecord<String, KafkaStockRequest> record = new ProducerRecord<>("stock-request-topic", cardId.toString(), request);

        RequestReplyFuture<String, KafkaStockRequest, KafkaStockReply> future = replyingKafkaTemplate.sendAndReceive(record);

        try {
            ConsumerRecord<String, KafkaStockReply> response = future.get(5, TimeUnit.SECONDS);
            return responseSuccess("requestStock", response.value());
        } catch (Exception e) {
            log.error("Stock request failed = {}", cardId, e);
            return responseError("requestStock", new KafkaReplyError("Stock error " + e.getMessage(), HttpStatus.GATEWAY_TIMEOUT));
        }
    }
}