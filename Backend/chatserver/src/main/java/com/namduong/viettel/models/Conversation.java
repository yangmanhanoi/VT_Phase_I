package com.namduong.viettel.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "conversation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "last_message")
    private LocalDateTime lastMsg;
    @Column(name = "is_expired")
    private Boolean isExpired;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Message> messages;

    public Conversation(User user, LocalDateTime lastMsg, Boolean isExpired) {
        this.user = user;
        this.lastMsg = lastMsg;
        this.isExpired = isExpired;
    }

    public void addMessage(Message msg)
    {
        if(messages == null) messages = new ArrayList<>();
        messages.add(msg);
    }
}
