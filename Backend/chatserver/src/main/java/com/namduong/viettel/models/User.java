package com.namduong.viettel.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class User {
    @Id
    private String id;
    @Column(name = "username")
    private String username;
    @Column(name = "enabled")
    private boolean enabled;
    @Column(name = "email")
    private String email;
    @Column(name = "first_name")
    private String firstname;
    @Column(name = "last_name")
    private String lastname;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "avatar_url")
    private String avatarUrl;


    public User(String id, String username, boolean enabled, String email, String firstname, String lastname, LocalDateTime createdAt, String avatarUrl) {
        this.id = id;
        this.username = username;
        this.enabled = enabled;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.createdAt = createdAt;
        this.avatarUrl = avatarUrl;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Conversation> conversations;

    public void addConversation(Conversation conv)
    {
        if(conversations == null) conversations = new ArrayList<>();
        conversations.add(conv);
    }
}
