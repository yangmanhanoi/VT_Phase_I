package com.namduong.viettel.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.retry.annotation.Retryable;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "conversation")
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userConv;
    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agentConv;
    @Column(name = "last_message")
    private LocalDateTime lastMsg;
    @Column(name = "is_expired")
    private Boolean isExpired;

    public Conversation(User user, LocalDateTime lastMsg, Boolean isExpired) {
        this.userConv = user;
        this.lastMsg = lastMsg;
        this.isExpired = isExpired;
    }
    public Conversation(User user, Agent agent, LocalDateTime lastMsg, Boolean isExpired)
    {
        this.userConv = user;
        this.agentConv = agent;
        this.lastMsg = lastMsg;
        this.isExpired = isExpired;
    }
}
