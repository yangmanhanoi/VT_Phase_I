package com.namduong.viettel.consumers;

import com.namduong.viettel.dtos.TicketPayload;
import com.namduong.viettel.models.Agent;
import com.namduong.viettel.models.Conversation;
import com.namduong.viettel.models.Ticket;
import com.namduong.viettel.models.User;
import com.namduong.viettel.producers.ChatProducer;
import com.namduong.viettel.services.AcdService;
import com.namduong.viettel.services.ConversationService;
import com.namduong.viettel.services.TicketService;
import com.namduong.viettel.services.UserService;
import com.namduong.viettel.utils.PageConstants;
import com.namduong.viettel.utils.TimeConverter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.ZoneId;

@Service
@Slf4j
public class ChatConsumer {
    @Autowired
    private TicketService ticketService;
    @Autowired
    private AcdService acdService;
    @Autowired
    private ChatProducer chatProducer;
    @Autowired
    private UserService userService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @KafkaListener(
            topics = PageConstants.PAGE_KAFKA_WEBHOOK_TICKET,
            groupId = "chatGroup"
    )
    public void consumeFromWebhook(String msg)
    {
        log.info(String.format("Consuming the message from webhook-ticketserver topic:: %s", msg));
        try{
            JSONObject obj = new JSONObject(msg);
            JSONArray entry = obj.optJSONArray("entry");
            JSONObject entryObject = entry.optJSONObject(0);
            JSONArray messagingArray = entryObject.optJSONArray("messaging");
            JSONObject messagingObject = messagingArray.optJSONObject(0);

            User user = userService.saveUser(messagingObject);
            log.info(user.toJSON().toString());
            Agent agent = user.getAgents().get(user.getAgents().size() - 1);
            if(agent != null)
            {
                acdService.addConvs(agent);
            }
            if(user != null)
            {
                messagingTemplate.convertAndSend("/topic/messages", user.toJSON().toString());
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
