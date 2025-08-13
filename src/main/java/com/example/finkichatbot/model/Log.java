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
    private PromptRequest prompt;
    @OneToOne
    private PromptResponse response;

    public Log(PromptRequest prompt, PromptResponse response) {
        this.prompt = prompt;
        this.response = response;
        this.promptEntryTime = LocalDateTime.now();
    }

    public LocalDateTime getPromptEntryTime() {
        return promptEntryTime;
    }

    public void setPromptEntryTime(LocalDateTime promptEntryTime) {
        this.promptEntryTime = promptEntryTime;
    }

    public PromptRequest getPrompt() {
        return prompt;
    }

    public void setPrompt(PromptRequest prompt) {
        this.prompt = prompt;
    }

    public PromptResponse getResponse() {
        return response;
    }

    public void setResponse(PromptResponse response) {
        this.response = response;
    }
}
