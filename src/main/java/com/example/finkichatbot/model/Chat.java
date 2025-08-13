package com.example.finkichatbot.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String chatName;

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    @ManyToOne
    private Group group;

    public Chat() {
    }

    public Chat(String chatName) {
        this.chatName = chatName;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
    public Long getId() {
        return id;
    }
}
