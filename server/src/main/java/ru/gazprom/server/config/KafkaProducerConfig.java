package ru.gazprom.server.config;

import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import ru.gazprom.server.dto.OrderDTO;

import java.util.Map;

@EnableKafka
@Configuration
public class KafkaProducerConfig {

    private final KafkaProperties kafkaProperties;

    public KafkaProducerConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @Bean
    public ProducerFactory<String, OrderDTO> producerFactory() {
        Map<String, Object> configProps = kafkaProperties.buildProducerProperties();

        return new DefaultKafkaProducerFactory<>(configProps);
    }


    @Bean
    public KafkaTemplate<String, OrderDTO> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
