package com.namduong.viettel.services.impls;

import com.namduong.viettel.dtos.MessageDTO;
import com.namduong.viettel.dtos.PayloadDTO;
import com.namduong.viettel.dtos.TicketPayload;
import com.namduong.viettel.services.TicketPayloadService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TicketPayloadServiceImpl implements TicketPayloadService {
    @Value("${page.id}")
    private String pageId;
    @Override
    public TicketPayload processData(JSONObject object, String conversationId) {
        JSONObject messageObject = object.optJSONObject("message");
        JSONArray attachmentJSONArray = messageObject.optJSONArray("attachments");
        String senderId = object.optJSONObject("sender").getString("id");
        String recipientId = object.optJSONObject("recipient").getString("id");
        Long timestamp = object.optLong("timestamp");
        MessageDTO messageDTO = MessageDTO
                .builder()
                .mid(messageObject.optString("mid"))
                .text(messageObject.optString("text"))
                .build();
        if(attachmentJSONArray != null)
        {
            attachmentJSONArray.forEach(data -> {
                String type = ((JSONObject)data).optString("type");
                JSONObject payload = ((JSONObject)data).optJSONObject("payload");
                String url = payload.optString("url");
                String stickerId = payload.optString("sticker_id");

                PayloadDTO p = PayloadDTO
                        .builder()
                        .type(type)
                        .url(url)
                        .sticker_id(stickerId)
                        .build();
                messageDTO.addPayload(p);
            });
        }

        return TicketPayload
                .builder()
                .conversationId(conversationId)
                .senderId(senderId)
                .recipientId(recipientId)
                .message(messageDTO)
                .timestamp(timestamp)
                .messageType((senderId != pageId) ? "" : "RESPONSE")
                .build();
    }
}
