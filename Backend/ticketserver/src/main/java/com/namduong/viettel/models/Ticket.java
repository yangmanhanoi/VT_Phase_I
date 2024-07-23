package com.namduong.viettel.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ticket")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne(mappedBy = "ticket")
    @JsonIgnore
    private User user;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "ticketMsg", cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private List<Message> messages;

    public Ticket(User user, LocalDateTime createdAt, List<Message> messages)
    {
        this.user = user;
        this.createdAt = createdAt;
        this.messages = messages;
    }
    public Ticket(LocalDateTime createdAt, List<Message> messages)
    {
        this.createdAt = createdAt;
        this.messages = messages;
    }
    public Ticket(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }
    public Ticket(User user,LocalDateTime createdAt)
    {
        this.user = user;
        this.createdAt = createdAt;

    }
    public void addMessage(Message m)
    {
        if(messages == null) messages = new ArrayList<>();
        messages.add(m);
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", messages=" + messages +
                '}';
    }
    public JSONObject toJSON()
    {
        JSONObject obj = new JSONObject();
        obj.put("id", this.id);
        obj.put("created_at", this.createdAt);
        JSONArray array = new JSONArray();
        if(this.messages != null)
        {
            for(Message message : messages)
            {
                array.put(message.toJSON());
            }
            obj.put("messages", array);
        }

        return obj;
    }
}
