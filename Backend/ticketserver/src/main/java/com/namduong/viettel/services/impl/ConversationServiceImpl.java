package com.namduong.viettel.services.impl;

import com.namduong.viettel.models.Agent;
import com.namduong.viettel.models.Conversation;
import com.namduong.viettel.models.Ticket;
import com.namduong.viettel.models.User;
import com.namduong.viettel.repositories.ConversationRepository;
import com.namduong.viettel.services.AcdService;
import com.namduong.viettel.services.AgentService;
import com.namduong.viettel.services.ConversationService;
import com.namduong.viettel.services.UserService;
import com.namduong.viettel.utils.PageConstants;
import com.namduong.viettel.utils.TimeConverter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Service
public class ConversationServiceImpl extends BaseRedisServiceImpl implements ConversationService {
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private AcdService acdService;
    @Autowired
    private AgentService agentService;

    public ConversationServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public Boolean isExpired(User user, Long timestamp) {
        String userId = user.getId();
        Optional<Conversation> optional = conversationRepository.findFirstByUserConvOrderByLastMsgDesc(user);
        Boolean isExpired = true;
        if(!optional.isEmpty())
        {
            LocalDateTime lastMessageTime = optional.get().getLastMsg();
            isExpired = calculateDifferenceInSeconds(lastMessageTime, timestamp);

        }
        return isExpired;
    }

    @Override
    public Conversation findById(Integer id) {
        Optional<Conversation> optional = conversationRepository.findById(id);
        if(!optional.isEmpty()) return optional.get();
        return null;
    }

    @Override
    public Conversation findByUserAndAgent(User user, Agent agent) {
        Optional<Conversation> optional = conversationRepository.findFirstByUserConvAndAgentConvOrderByLastMsgDesc(user, agent);
        if(!optional.isEmpty()) return optional.get();
        return null;
    }

    @Override
    public Conversation save(JSONObject object) {
        JSONObject messageObject = object.optJSONObject("message");

        String senderId = object.optJSONObject("sender").getString("id");
        String recipientId = object.optJSONObject("recipient").getString("id");
        Long timestamp = object.optLong("timestamp");

        String userId = (senderId != PageConstants.PAGE_ID) ? senderId : recipientId;
        User user = userService.findUserById(userId, object);
        Agent agent = getAgentIdInRedisIfExist(user);
        Conversation conversation = null;
        if(agent == null)
        {
            agent = acdService.findAgent();
            conversation = new Conversation(user, agent, TimeConverter.convertTimestampToLocalDateTime(timestamp, ZoneId.of("UTC")), false);

        }
        else{
            String id = (String) this.hashGet(userId, "conversation");
            conversation = findById(Integer.parseInt(id));
        }
        saveUserAndAgent2Redis(conversation);
        return conversationRepository.save(conversation);
    }

    private Agent getAgentIdInRedisIfExist(User user)
    {
        Agent agent = null;
        if(this.hashGet(user.getId(), PageConstants.PAGE_REDIS_FIELD_AGENT) != null)
        {
            String agentId = (String) this.hashGet(user.getId(), PageConstants.PAGE_REDIS_FIELD_AGENT);
            agent = agentService.findById(agentId);
            this.hashSet(String.format("shadowKey:%s", user.getId()), PageConstants.PAGE_REDIS_FIELD_AGENT, agent);
            this.setTimeToLiveInMinutes(user.getId(), 5L);
        }
        return agent;
    }
    public Conversation saveUserAndAgent2Redis(Conversation conversation) {
        if(conversation.getAgentConv() != null && conversation.getAgentConv() != null)
        {
            String shadowConv = String.format("shadowKey:%s", conversation.getUserConv().getId());
            this.hashSet(conversation.getAgentConv().getId(), "conversation", conversation.getId());
            this.hashSet(conversation.getAgentConv().getId(), PageConstants.PAGE_REDIS_FIELD_AGENT, conversation.getAgentConv().getId());

            this.hashSet(shadowConv, "conversation", conversation.getId());
            this.hashSet(shadowConv, PageConstants.PAGE_REDIS_FIELD_AGENT, conversation.getAgentConv().getId());
            this.setTimeToLiveInMinutes(shadowConv, 5L);

        }
        return conversation;
    }
    // TODO: test so set the timeout to 60s as 1 minute => actually it 10 minutes
    private Boolean calculateDifferenceInSeconds(LocalDateTime dateTime, Long timestamp)
    {
        long localDateTimeMillis = TimeConverter.convertLocalDateTimeToTimestamp(dateTime, ZoneOffset.UTC);
        long differenceMillis = Math.abs(localDateTimeMillis - timestamp);
        long differenceSecond =  differenceMillis / 1000;
        return differenceSecond <= 60 ? false : true;
    }
}
