package com.namduong.viettel.services;

import com.namduong.viettel.models.Agent;
import com.namduong.viettel.models.Conversation;
import com.namduong.viettel.models.User;
import org.json.JSONObject;

public interface ConversationService {
    Boolean isExpired(User user, Long timestamp);

    Conversation findById(Integer id);

    Conversation findByUserAndAgent(User user, Agent agent);

    Conversation save(JSONObject jsonObject);
}
