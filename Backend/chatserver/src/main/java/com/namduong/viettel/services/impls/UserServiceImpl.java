package com.namduong.viettel.services.impls;

import com.namduong.viettel.exceptions.UserNotFoundException;
import com.namduong.viettel.models.*;
import com.namduong.viettel.repositories.UserRepository;
import com.namduong.viettel.services.ConversationService;
import com.namduong.viettel.services.UserService;
import com.namduong.viettel.utils.TimeConverter;
import com.namduong.viettel.models.Conversation;
import com.namduong.viettel.models.Message;
import com.namduong.viettel.models.Payload;
import com.namduong.viettel.models.User;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConversationService conversationService;
    private AtomicInteger customIdCounter = new AtomicInteger(0);
    @Value("${facebook.page.access.token}")
    private String accessToken;
    @Value("${page.id}")
    private String pageId;
    private String generateCustomId()
    {
        Integer id = customIdCounter.incrementAndGet();
        return String.format("ipcc_%03d", id);
    }
    @Override
    public User save(String id) {
        if(id == null)
        {
            id = generateCustomId();
        }
        else {
            Optional<User> optional = userRepository.findById(id);
            if(!optional.isEmpty())
            {
                return optional.get();
            }
            String url = "https://graph.facebook.com/" + id +
                    "?fields=id,name,first_name,last_name,profile_pic&access_token=" + accessToken;
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);
            JSONObject jsonObject = new JSONObject(response);
            User user = new User(id, jsonObject.getString("name"), true, null,
                    jsonObject.getString("first_name"), jsonObject.getString("last_name"), LocalDateTime.now(),
                    jsonObject.getString("profile_pic"));
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    @Transactional
    public User save(JSONObject messagingObject) {
        JSONObject messageObject = messagingObject.optJSONObject("message");
        JSONArray attachmentJSONArray = messageObject.optJSONArray("attachments");
        String senderId = messagingObject.optJSONObject("sender").getString("id");
        String recipientId = messagingObject.optJSONObject("recipient").getString("id");
        Long timestamp = messagingObject.optLong("timestamp");

        Message message = new Message(messageObject.getString("mid"),senderId, recipientId, TimeConverter.convertTimestampToLocalDateTime(timestamp, ZoneId.of("UTC")), messageObject.optString("text"));
        if(attachmentJSONArray != null)
        {
            attachmentJSONArray.forEach(data -> {
                String type = ((JSONObject)data).optString("type");
                JSONObject payload = ((JSONObject)data).optJSONObject("payload");
                String url = payload.optString("url");
                String stickerId = payload.optString("sticker_id");

                Payload p = new Payload(url, stickerId, type, message);
                message.addPayload(p);
            });
        }
        if(senderId == null)
        {
            senderId = generateCustomId();
        }
        else
        {
            Optional<User> optional = userRepository.findById(senderId);
            if(!optional.isEmpty())
            {
                User user = optional.get();
                // Check if the conversation is expired
                Boolean isExpired = conversationService.checkExpired(user, timestamp);
                Conversation conversation = null;
                // Case when no conversation before or the previous conversation is expired
                if(user.getConversations() == null || (user.getConversations() != null && isExpired))
                {
                    if(user.getConversations() != null) user.getConversations().forEach(conv -> conv.setIsExpired(true));
                    conversation = new Conversation(user, TimeConverter.convertTimestampToLocalDateTime(timestamp, ZoneId.of("UTC")),
                            false);
                    message.setConversation(conversation);
                    conversation.addMessage(message);
                    user.addConversation(conversation);
                }
                else{
                    conversation = user.getConversations().get(user.getConversations().size() - 1);
                    conversation.setIsExpired(isExpired);
                    if(!isExpired)
                    {
                        conversation.setLastMsg(TimeConverter.convertTimestampToLocalDateTime(timestamp, ZoneId.of("UTC")));
                    }
                    message.setConversation(conversation);
                    conversation.addMessage(message);
                }
                return userRepository.save(user);
            }
            // case when user first send message then store new user
            String url = "https://graph.facebook.com/" + senderId +
                    "?fields=id,name,first_name,last_name,profile_pic&access_token=" + accessToken;
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);
            JSONObject jsonObject = new JSONObject(response);
            User user = new User(senderId, jsonObject.getString("name"), true, null,
                    jsonObject.getString("first_name"), jsonObject.getString("last_name"), LocalDateTime.now(),
                    jsonObject.getString("profile_pic"));
            Conversation conversation = new Conversation(user, TimeConverter.convertTimestampToLocalDateTime(timestamp, ZoneId.of("UTC")), false);
            message.setConversation(conversation);
            conversation.addMessage(message);
            user.addConversation(conversation);
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public User findByUserId(String id) throws UserNotFoundException {
        Optional<User> optional = userRepository.findById(id);
        if(optional.isEmpty()){
            throw new UserNotFoundException(String.format("User with id: %s not found", id));
        }
        return optional.get();
    }
}
