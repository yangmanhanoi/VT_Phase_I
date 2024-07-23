package com.namduong.viettel.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;

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
    @JoinColumn(name = "ticket_id")
    private Ticket ticketMsg;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Payload> payloads;

    public Message(String id, String senderId, String recipientId, LocalDateTime timestamp, String text, Ticket ticket) {
        this.id = id;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.timestamp = timestamp;
        this.text = text;
        this.ticketMsg = ticket;
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

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", senderId='" + senderId + '\'' +
                ", recipientId='" + recipientId + '\'' +
                ", timestamp=" + timestamp +
                ", text='" + text + '\'' +
                ", payload=" + payloads +
                '}';
    }
    public JSONObject toJSON()
    {
        JSONObject obj = new JSONObject();
        obj.put("id", this.id);
        obj.put("sender_id", this.senderId);
        obj.put("recipient_id", this.recipientId);
        obj.put("timestamp", this.timestamp);
        obj.put("text", this.text);
        JSONArray array = new JSONArray();
        if(this.payloads != null)
        {
            for (Payload p : payloads)
            {
                array.put(p.toJSON());
            }
            obj.put("payloads", array);
        }


        return obj;
    }
}
