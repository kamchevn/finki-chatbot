package com.example.finkichatbot.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String groupName;

    @OneToMany(mappedBy = "group")
    private List<Chat> chats;

    public Group(String groupName, List<Chat> chats) {
        this.groupName = groupName;
        this.chats = chats;
    }

    public Group(String groupName) {
        this.groupName = groupName;
        this.chats = new ArrayList<>();
    }
}
