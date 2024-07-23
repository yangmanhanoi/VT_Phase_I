package com.namduong.viettel.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TicketPayload {
    private String conversationId;
    private String senderId;
    private String recipientId;
    private String messageType;
    private Long timestamp;
    private MessageDTO message;
}
