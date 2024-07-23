package com.namduong.viettel.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "agent")
public class Agent {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "enabled")
    private Boolean enabled;
    @Column(name = "phone")
    private String phone;
    @Column(name = "avatar_url")
    private String avatarUrl;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "acd_id")
    private Acd acd;

    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "agents"
    )
    @JsonIgnore
    private List<User> users;

    public void addUser(User user)
    {
        if(users == null) users = new ArrayList<>();
        users.add(user);
    }

    @Override
    public String toString() {
        return "Agent{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", phone='" + phone + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }
}
