package com.namduong.viettel.services;

import com.namduong.viettel.dtos.TicketPayload;
import org.json.JSONObject;

public interface TicketPayloadService {
    TicketPayload processData(JSONObject object, String conversationId);
}
