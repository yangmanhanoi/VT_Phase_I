package com.namduong.viettel.services;

import com.namduong.viettel.models.User;
import org.json.JSONArray;
import org.json.JSONObject;

public interface UserService {
    User findUserById(String id, JSONObject obj);

    User saveUser(JSONObject object);

    String getAgentIdInRedis(String userId);
}
