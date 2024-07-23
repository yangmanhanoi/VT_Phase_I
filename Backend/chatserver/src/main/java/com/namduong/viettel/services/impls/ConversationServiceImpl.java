package com.namduong.viettel.services.impls;

import com.namduong.viettel.models.Conversation;
import com.namduong.viettel.models.User;
import com.namduong.viettel.repositories.ConversationRepository;
import com.namduong.viettel.services.ConversationService;
import com.namduong.viettel.utils.TimeConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
@Slf4j
public class ConversationServiceImpl implements ConversationService {
    @Autowired
    private ConversationRepository conversationRepository;
    @Override
    public Boolean checkExpired(User user, Long timestamp) {
        String userId = user.getId();
        Optional<Conversation> optional = conversationRepository.findFirstByUserOrderByLastMsgDesc(user);
        Boolean isExpired = true;
        if(!optional.isEmpty())
        {
            LocalDateTime lastMessageTime = optional.get().getLastMsg();
            isExpired = calculateDifferenceInSeconds(lastMessageTime, timestamp);

        }
        return isExpired;
    }

    private Boolean calculateDifferenceInSeconds(LocalDateTime dateTime, Long timestamp)
    {
        long localDateTimeMillis = TimeConverter.convertLocalDateTimeToTimestamp(dateTime, ZoneOffset.UTC);
        long differenceMillis = Math.abs(localDateTimeMillis - timestamp);
        long differenceSecond =  differenceMillis / 1000;
        return differenceSecond <= 60 ? false : true;
    }
}
