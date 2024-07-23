package com.namduong.viettel.configs;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic webChat(){
        return TopicBuilder
                .name("webhook-chatserver")
                .build();
    }

    @Bean NewTopic chatTicket(){
        return TopicBuilder
                .name("chatserver-ticketserver")
                .build();
    }


}
