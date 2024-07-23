package com.namduong.viettel.consumers;

import com.namduong.viettel.dtos.TicketPayload;
import com.namduong.viettel.models.User;
import com.namduong.viettel.producers.KafkaProducer;
import com.namduong.viettel.services.TicketPayloadService;
import com.namduong.viettel.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumer {
    @Autowired
    private UserService userService;
    @Autowired
    private KafkaProducer kafkaProducer;
    @Autowired
    private TicketPayloadService ticketPayloadService;
    @KafkaListener(
            topics = "webhook-chatserver",
            groupId = "myGroup"
    )
    public void consumeMsg(String msg){
        log.info(String.format("Consuming the message from webhook-chatserver topic:: %s", msg));
        try {
            JSONObject obj = new JSONObject(msg);
            JSONArray entry = obj.optJSONArray("entry");
            JSONObject entryObject = entry.optJSONObject(0);
            JSONArray messagingArray = entryObject.optJSONArray("messaging");
            JSONObject messagingObject = messagingArray.optJSONObject(0);
            //userService.save((String) senderObject.get("id"));
            User user =  userService.save(messagingObject);
            String conversationId = String.format("conversation_%s", user.getConversations().get(user.getConversations().size() - 1).getId().toString());
            TicketPayload payload = ticketPayloadService.processData(messagingObject, conversationId);
            log.info(payload.toString());
            kafkaProducer.sendMessage(payload);
        }catch (Exception e)
        {
            log.error(e.toString());
        }

    }
}
