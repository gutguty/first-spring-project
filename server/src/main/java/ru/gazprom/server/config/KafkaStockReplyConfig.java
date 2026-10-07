package ru.gazprom.server.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import ru.gazprom.server.dto.KafkaStockReply;
import ru.gazprom.server.dto.KafkaStockRequest;

import java.time.Duration;
import java.util.Map;

@Configuration
public class KafkaStockReplyConfig {

    private final KafkaProperties kafkaProperties;

    public KafkaStockReplyConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @Bean
    public ProducerFactory<String, KafkaStockRequest> stockRequestProducerFactory() {
        Map<String, Object> props = kafkaProperties.buildProducerProperties();
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public ConsumerFactory<String, KafkaStockReply> stockReplyConsumerFactory() {
        Map<String, Object> props = kafkaProperties.buildConsumerProperties();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "server-reply-group");
        props.put(JacksonJsonDeserializer.TRUSTED_PACKAGES, "ru.gazprom.server.dto");
        props.put(JacksonJsonDeserializer.VALUE_DEFAULT_TYPE, "ru.gazprom.server.dto.KafkaStockReply");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, KafkaStockReply> replyContainer(
            ConsumerFactory<String, KafkaStockReply> stockReplyConsumerFactory) {

        ContainerProperties containerProperties = new ContainerProperties("stock-reply-topic");
        return new ConcurrentMessageListenerContainer<>(stockReplyConsumerFactory, containerProperties);
    }

    @Bean
    public ReplyingKafkaTemplate<String, KafkaStockRequest, KafkaStockReply> replyReplyingKafkaTemplate(
            ProducerFactory<String, KafkaStockRequest> stockRequestProducerFactory,
            ConcurrentMessageListenerContainer<String, KafkaStockReply> replyContainer) {

        ReplyingKafkaTemplate<String, KafkaStockRequest, KafkaStockReply> template =
                new ReplyingKafkaTemplate<>(stockRequestProducerFactory, replyContainer);
        template.setDefaultReplyTimeout(Duration.ofSeconds(5));

        return template;
    }
}