package com.namduong.viettel.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "message")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "sender_id")
    private String senderId;
    @Column(name = "recipient_id")
    private String recipientId;
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
    @Column(name = "text")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL)
    private List<Payload> payloads;

    public Message(String id, String senderId, String recipientId, LocalDateTime timestamp, String text, Conversation conversation) {
        this.id = id;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.timestamp = timestamp;
        this.text = text;
        this.conversation = conversation;
    }

    public Message(String id, String senderId, String recipientId, LocalDateTime timestamp, String text) {
        this.id = id;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.timestamp = timestamp;
        this.text = text;
    }

    public void addPayload(Payload payload)
    {
        if(payloads == null) payloads = new ArrayList<>();
        payloads.add(payload);
    }
}
