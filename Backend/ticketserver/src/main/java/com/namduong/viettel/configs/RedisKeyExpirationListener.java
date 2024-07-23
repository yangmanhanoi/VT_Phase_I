package com.namduong.viettel.configs;

import com.namduong.viettel.models.Agent;
import com.namduong.viettel.services.AcdService;
import com.namduong.viettel.services.AgentService;
import com.namduong.viettel.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {
    @Autowired
    private UserService userService;
    @Autowired
    private AgentService agentService;
    @Autowired
    private AcdService acdService;
    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }
    @Override
    public void onMessage(Message message, byte[] pattern) {
        Pattern pattern1 = Pattern.compile("shadowKey:(.*)");
        log.info(message.toString());
        Matcher matcher = pattern1.matcher(message.toString());
        if(matcher.matches())
        {
            String userId = matcher.group(1);

            String agentId = userService.getAgentIdInRedis(userId);
            if(agentId != null)
            {
                log.info(agentId);
                Agent agent =  agentService.findById(agentId);
                acdService.decreaseCurrentConv(agent);
            }
        }
        super.onMessage(message, pattern);
    }
}
