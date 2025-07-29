package com.example.finkichatbot.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Prompt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String prompt;
    private String response;

    @ManyToOne
    private Chat chat;

    public Prompt(String prompt, String response) {
        this.prompt = prompt;
        this.response = response;
    }

}
