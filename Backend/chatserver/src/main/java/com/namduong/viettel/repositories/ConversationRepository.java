package com.namduong.viettel.repositories;

import com.namduong.viettel.models.Conversation;
import com.namduong.viettel.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Integer> {
    Optional<Conversation> findFirstByUserOrderByLastMsgDesc(User user);
}
