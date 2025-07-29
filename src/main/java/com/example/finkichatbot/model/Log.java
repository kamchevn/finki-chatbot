package com.example.finkichatbot.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime promptEntryTime;
    @OneToOne
    private Prompt prompt;

    public Log(Prompt prompt) {
        this.prompt = prompt;
        this.promptEntryTime = LocalDateTime.now();
    }
}
