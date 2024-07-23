package com.namduong.viettel.producers;

import com.namduong.viettel.dtos.TicketPayload;
import com.namduong.viettel.models.Conversation;
import com.namduong.viettel.models.Ticket;
import com.namduong.viettel.utils.PageConstants;
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
public class ChatProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void requestFindAcd(Conversation conversation)
    {
        Message<Conversation> message = MessageBuilder
                .withPayload(conversation)
                        .setHeader(KafkaHeaders.TOPIC, PageConstants.PAGE_KAFKA_TICKET_ACD)
                                .build();
        kafkaTemplate.send(message);
    }

    public void sendSuitableAgent(Ticket ticket)
    {
        Message<Ticket> message = MessageBuilder
                .withPayload(ticket)
                        .setHeader(KafkaHeaders.TOPIC, PageConstants.PAGE_KAFKA_TICKET_AGENT)
                                .build();
        kafkaTemplate.send(message);
    }
}
