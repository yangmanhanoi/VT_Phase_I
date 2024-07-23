package com.namduong.viettel.configs;

import com.namduong.viettel.utils.PageConstants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic webhookTicket()
    {
        return TopicBuilder
                .name(PageConstants.PAGE_KAFKA_WEBHOOK_TICKET)
                .build();
    }
}
