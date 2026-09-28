package ru.gazprom.server.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic stockOrderTopic() {
        return TopicBuilder.name("order-created-topic")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic stockRequestTopic() {
        return TopicBuilder.name("stock-request-topic")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic stockReplyTopic() {
        return TopicBuilder.name("stock-reply-topic")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
