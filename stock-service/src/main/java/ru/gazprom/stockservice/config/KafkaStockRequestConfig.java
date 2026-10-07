package ru.gazprom.stockservice.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
import ru.gazprom.stockservice.dto.KafkaStockReply;
import ru.gazprom.stockservice.dto.KafkaStockRequest;

import java.util.Map;

@Configuration
public class KafkaStockRequestConfig {

    private final KafkaProperties kafkaProperties;

    public KafkaStockRequestConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @Bean
    public ProducerFactory<String, KafkaStockReply> stockReplyProducerFactory() {
        Map<String, Object> props = kafkaProperties.buildProducerProperties();
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public ConsumerFactory<String, KafkaStockRequest> stockRequestConsumerFactory() {
        Map<String, Object> props = kafkaProperties.buildConsumerProperties();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "stock-service-request-group");
        props.put(JacksonJsonDeserializer.TRUSTED_PACKAGES, "ru.gazprom.stockservice.dto");
        props.put(JacksonJsonDeserializer.VALUE_DEFAULT_TYPE, "ru.gazprom.stockservice.dto.KafkaStockRequest");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, KafkaStockRequest> stockRequestKafkaListenerContainerFactory(
            KafkaTemplate<String, KafkaStockReply> stockReplyKafkaTemplate) {
        ConcurrentKafkaListenerContainerFactory<String, KafkaStockRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(stockRequestConsumerFactory());
        factory.setReplyTemplate(stockReplyKafkaTemplate);
        return factory;
    }

    @Bean
    public KafkaTemplate<String, KafkaStockReply> stockReplyKafkaTemplate() {
        return new KafkaTemplate<>(stockReplyProducerFactory());
    }
}