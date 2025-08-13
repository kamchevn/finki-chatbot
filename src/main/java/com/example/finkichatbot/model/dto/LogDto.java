package com.example.finkichatbot.model.dto;

import com.example.finkichatbot.model.Log;

import java.time.LocalDateTime;

public record LogDto(
        PromptRequestDto requestDto,
        PromptResponseDto responseDto,
        LocalDateTime promptTime) {
}
