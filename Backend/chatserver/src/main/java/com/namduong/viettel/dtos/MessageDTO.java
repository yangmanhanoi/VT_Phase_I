package com.namduong.viettel.dtos;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MessageDTO {
    private String mid;
    private String text;
    private List<PayloadDTO> attachments;

    public void addPayload(PayloadDTO payload)
    {
        if(attachments == null) attachments = new ArrayList<>();
        attachments.add(payload);
    }
}
