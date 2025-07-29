package com.example.finkichatbot.service;

import com.example.finkichatbot.model.Prompt;

import java.util.List;

public interface PromptService {
    List<Prompt> getPromptsFromChat(Long chatId);
    Prompt createPrompt(String prompt, String response, Long chatId);
    Prompt editPrompt(Long id, String prompt, String response, Long chatId);
    Prompt deletePrompt(Long id);
}
