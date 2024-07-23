package com.namduong.viettel.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

@Entity
@Table(name = "payload")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payload {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "url")
    private String url;
    @Column(name = "sticker_id")
    private String stickerId;
    @Column(name = "type")
    private String type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "message_id")
    private Message message;

    public Payload(String url, String stickerId, String type, Message message) {
        this.url = url;
        this.stickerId = stickerId;
        this.type = type;
        this.message = message;
    }
    public JSONObject toJSON()
    {
        JSONObject obj = new JSONObject();
        obj.put("url", this.url);
        obj.put("type", this.type);
        return obj;
    }
}
