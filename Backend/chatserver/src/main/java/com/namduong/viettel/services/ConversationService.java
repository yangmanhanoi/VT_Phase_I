package com.namduong.viettel.services;

import com.namduong.viettel.models.User;

public interface ConversationService {
    Boolean checkExpired(User user, Long timestamp);
}
