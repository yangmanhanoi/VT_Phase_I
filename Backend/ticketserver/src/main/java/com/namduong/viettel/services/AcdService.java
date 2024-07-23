package com.namduong.viettel.services;

import com.namduong.viettel.models.Acd;
import com.namduong.viettel.models.Agent;
import com.namduong.viettel.models.Conversation;

public interface AcdService {
//    String getAgentIdInRedis(String conversationId, Long timestamp);
//
//    Conversation saveConversation2Redis(Conversation conversation);

    Agent findAgent();
    Acd addConvs(Agent agent);
    Acd decreaseCurrentConv(Agent agent);

}
