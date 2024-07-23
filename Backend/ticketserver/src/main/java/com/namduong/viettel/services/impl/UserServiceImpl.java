package com.namduong.viettel.services.impl;

import com.namduong.viettel.models.*;
import com.namduong.viettel.repositories.UserRepository;
import com.namduong.viettel.services.AcdService;
import com.namduong.viettel.services.AgentService;
import com.namduong.viettel.services.UserService;
import com.namduong.viettel.utils.PageConstants;
import com.namduong.viettel.utils.TimeConverter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class UserServiceImpl extends BaseRedisServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AcdService acdService;
    @Autowired
    private AgentService agentService;
    private AtomicInteger customIdCounter = new AtomicInteger(0);

    public UserServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate);
    }

    private String generateCustomId()
    {
        Integer id = customIdCounter.incrementAndGet();
        return String.format("ipcc_%03d", id);
    }
    @Override
    public User findUserById(String id, JSONObject obj) {
        JSONObject messageObject = obj.optJSONObject("message");
        JSONArray attachmentJSONArray = obj.optJSONArray("attachments");
        Message message = new Message(messageObject.optString("mid"),
                obj.optJSONObject("sender").getString("id"),
                obj.optJSONObject("recipient").getString("id"),
                TimeConverter.convertTimestampToLocalDateTime(obj.optLong("timestamp"), ZoneId.of("UTC")),
                messageObject.optString("text"));
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
        User user = null;
        Optional<User> optional = userRepository.findById(id);
        if(!optional.isEmpty()) {
            user = optional.get();
        }
        else{
            String url = "https://graph.facebook.com/" + id +
                    "?fields=id,name,first_name,last_name,profile_pic&access_token=" + PageConstants.PAGE_ACCESS_TOKEN;
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);
            JSONObject jsonObject = new JSONObject(response);
            user = new User(id, jsonObject.getString("name"), true, null,
                    jsonObject.getString("first_name"), jsonObject.getString("last_name"), LocalDateTime.now(),
                    jsonObject.getString("profile_pic"));
        }
        Ticket ticket = new Ticket(TimeConverter.convertTimestampToLocalDateTime(obj.optLong("timestamp"), ZoneId.of("UTC")));
        message.setTicketMsg(ticket);
        ticket.addMessage(message);
        user.setTicket(ticket);
        return userRepository.saveAndFlush(user);
    }

    @Override
    @Transactional
    public User saveUser(JSONObject messagingObject) {
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
        }else{
            Optional<User> optional = userRepository.findById((senderId.equals(PageConstants.PAGE_ID)) ? recipientId : senderId);
            User user = null;
            if(!optional.isEmpty())
            {
                user = optional.get();
                String agentId = getAgentIdInRedisIfExist(user);
                // check if conversation is expired
                //Boolean isExpired = conversationService.isExpired(user, timestamp);
                Agent agent = null;
                Ticket ticket = user.getTicket();
                // Case when no conversation before or the previous conversation is expired
                if(agentId == null)
                {
                    agent = acdService.findAgent();
                    user.addAgent(agent);
                }else{
                    agent = agentService.findById(agentId);
                    log.info(agentId + "_" + agent.toString());
                }
                message.setTicketMsg(ticket);
                ticket.addMessage(message);
                saveUserAndAgent2Redis(user, agent, 5L);
                return userRepository.save(user);

            }
            // case when user first send message then store new user
            String url = "https://graph.facebook.com/" + senderId +
                    "?fields=id,name,first_name,last_name,profile_pic&access_token=" + PageConstants.PAGE_ACCESS_TOKEN;
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);
            JSONObject jsonObject = new JSONObject(response);
            user = new User(senderId, jsonObject.getString("name"), true, null,
                    jsonObject.getString("first_name"), jsonObject.getString("last_name"), LocalDateTime.now(),
                    jsonObject.getString("profile_pic"));
            Ticket ticket = new Ticket(TimeConverter.convertTimestampToLocalDateTime(timestamp, ZoneId.of("UTC")));
            message.setTicketMsg(ticket);
            ticket.addMessage(message);
            Agent agent = acdService.findAgent();
            user.addAgent(agent);
            user.setTicket(ticket);
            saveUserAndAgent2Redis(user, agent, 5L);
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public String getAgentIdInRedis(String userId) {
        String agentId = null;
        if(this.hashGet(userId, PageConstants.PAGE_REDIS_FIELD_AGENT) != null)
        {
            agentId = (String) this.hashGet(userId, PageConstants.PAGE_REDIS_FIELD_AGENT);
            delete(userId);
        }
        return agentId;
    }

    // Save user with agent to redis
    // if user has already saved in redis then he/she is served by the recorded agent
    private void saveUserAndAgent2Redis(User user, Agent agent, Long timeInMinutes){
        if(user != null && agent != null)
        {
            this.hashSet(user.getId(), PageConstants.PAGE_REDIS_FIELD_AGENT, agent.getId());
            this.hashSet(String.format("shadowKey:%s", user.getId()), PageConstants.PAGE_REDIS_FIELD_AGENT, agent.getId());
            this.setTimeToLiveInMinutes(String.format("shadowKey:%s", user.getId()), timeInMinutes);
        }
    }
    private String getAgentIdInRedisIfExist(User user)
    {
        String agentId = null;
        if(this.hashGet(user.getId(), PageConstants.PAGE_REDIS_FIELD_AGENT) != null)
        {
            agentId = (String) this.hashGet(user.getId(), PageConstants.PAGE_REDIS_FIELD_AGENT);
            this.hashSet(String.format("shadowKey:%s", user.getId()), PageConstants.PAGE_REDIS_FIELD_AGENT, agentId);
            this.setTimeToLiveInMinutes(user.getId(), 5L);
        }
        return agentId;
    }
}
