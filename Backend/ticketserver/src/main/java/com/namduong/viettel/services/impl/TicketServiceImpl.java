package com.namduong.viettel.services.impl;

import com.namduong.viettel.dtos.MessageDTO;
import com.namduong.viettel.dtos.PayloadDTO;
import com.namduong.viettel.dtos.TicketPayload;
import com.namduong.viettel.models.Message;
import com.namduong.viettel.models.Payload;
import com.namduong.viettel.models.Ticket;
import com.namduong.viettel.models.User;
import com.namduong.viettel.repositories.TicketRepository;
import com.namduong.viettel.services.TicketService;
import com.namduong.viettel.services.UserService;
import com.namduong.viettel.utils.PageConstants;
import com.namduong.viettel.utils.TimeConverter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserService userService;


    @Override
    public Ticket saveData(TicketPayload payload) {
//        String senderId = payload.getSenderId();
//        String recipientId = payload.getRecipientId();
//        String userId = (senderId != PageConstants.PAGE_ID) ? senderId : recipientId;
//        String msgType = payload.getMessageType();
//        Long timestamp = payload.getTimestamp();
//        MessageDTO msg = payload.getMessage();
//        User user = userService.findUserById(userId);
//        if(user == null)
//        {
//            String url = "https://graph.facebook.com/" + userId +
//                    "?fields=id,name,first_name,last_name,profile_pic&access_token=" + PageConstants.PAGE_ACCESS_TOKEN;
//            RestTemplate restTemplate = new RestTemplate();
//            String response = restTemplate.getForObject(url, String.class);
//            JSONObject jsonObject = new JSONObject(response);
//            user = User
//                    .builder()
//                    .id(userId)
//                    .username(jsonObject.optString("name"))
//                    .firstname(jsonObject.optString("first_name"))
//                    .lastname(jsonObject.optString("last_name"))
//                    .email(null)
//                    .enabled(true)
//                    .avatarUrl(jsonObject.optString("profile_pic"))
//                    .createdAt(LocalDateTime.now())
//                    .build();
//        }
//        Ticket ticket = ticketRepository.findByUser(user);
//        if(ticket == null)
//        {
//            ticket = new Ticket(user, LocalDateTime.now());
//        }
//        Message message = new Message(msg.getMid(), senderId, recipientId, TimeConverter.convertTimestampToLocalDateTime(timestamp, ZoneId.of("UTC")), msg.getText());
//        message.setTicket(ticket);
//        List<PayloadDTO> payloads = msg.getAttachments();
//        if(payloads != null)
//        {
//            for(PayloadDTO pDTO: payloads)
//            {
//                Payload p = new Payload(pDTO.getUrl(), pDTO.getSticker_id(), pDTO.getType(), message);
//                message.addPayload(p);
//            }
//        }
//        ticket.addMessage(message);
//        return ticketRepository.save(ticket);
        return  null;
    }
}
