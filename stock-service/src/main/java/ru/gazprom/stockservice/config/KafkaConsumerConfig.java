package ru.gazprom.stockservice.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import ru.gazprom.stockservice.dto.OrderDTO;

import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    private final KafkaProperties kafkaProperties;

    public KafkaConsumerConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    public Map<String, Object> consumerConfig(String groupId) {
        Map<String, Object> configProps = kafkaProperties.buildConsumerProperties();
        configProps.put(JacksonJsonDeserializer.TRUSTED_PACKAGES, "ru.gazprom.stockservice.dto");
        configProps.put(JacksonJsonDeserializer.VALUE_DEFAULT_TYPE, "ru.gazprom.stockservice.dto.OrderDTO");
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        return configProps;
    }

    public ConsumerFactory<String, OrderDTO> consumerFactory(String groupId) {
        return new DefaultKafkaConsumerFactory<>(consumerConfig(groupId));
    }

    public ConcurrentKafkaListenerContainerFactory<String, OrderDTO> kafkaListenerContainerFactory(String groupId) {
        ConcurrentKafkaListenerContainerFactory<String, OrderDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(groupId));
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderDTO> stockKafkaListenerContainerFactory() {
        return kafkaListenerContainerFactory("stock-service-group_id");
    }
}
