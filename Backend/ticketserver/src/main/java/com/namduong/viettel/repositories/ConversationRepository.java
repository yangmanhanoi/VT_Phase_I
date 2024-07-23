package com.namduong.viettel.repositories;

import com.namduong.viettel.models.Agent;
import com.namduong.viettel.models.Conversation;
import com.namduong.viettel.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Integer> {

    Optional<Conversation> findFirstByUserConvOrderByLastMsgDesc(User user);

    Optional<Conversation> findFirstByUserConvAndAgentConvOrderByLastMsgDesc(User user, Agent agent);
}
