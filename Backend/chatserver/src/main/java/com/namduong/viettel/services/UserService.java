package com.namduong.viettel.services;

import com.namduong.viettel.exceptions.UserNotFoundException;
import com.namduong.viettel.models.User;
import org.json.JSONObject;

public interface UserService {
    User save(String id);
    User save(JSONObject jsonObject);
    User findByUserId(String id) throws UserNotFoundException;
}
