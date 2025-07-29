package com.example.finkichatbot.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String chatName;

    @ManyToOne
    private Group group;

    public Chat(String chatName, Group group) {
        this.chatName = chatName;
        this.group = group;
    }
    public Chat(String chatName) {
        this.chatName = chatName;
    }
}
