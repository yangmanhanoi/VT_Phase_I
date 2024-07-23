package com.namduong.viettel.producers;

import com.namduong.viettel.dtos.TicketPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, TicketPayload> kafkaTemplate;
    public void sendMessage(TicketPayload payload)
    {
        Message<TicketPayload> message = MessageBuilder
                .withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC, "chatserver-ticketserver")
                .build();
        kafkaTemplate.send(message);
    }
}
