package com.example.finkichatbot.model.dto;

public record PromptRequestDto(
        String model,
        String prompt,
        boolean stream,
        Long chatId) {
}
