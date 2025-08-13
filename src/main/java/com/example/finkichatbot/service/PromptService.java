package com.example.finkichatbot.service;

import com.example.finkichatbot.model.PromptRequest;
import com.example.finkichatbot.model.PromptResponse;

import java.util.List;

public interface PromptService {
    PromptRequest findByPrompt(String prompt);
    List<PromptRequest> getRequestsFromChat(Long chatId);
    List<PromptResponse> getResponsesFromChat(Long chatId);
    PromptRequest createPrompt(String prompt, Long chatId);
    PromptResponse askOllama(PromptRequest promptRequest);
    PromptRequest editPrompt(String oldPrompt, String newPrompt, Long chatId);
    PromptResponse askOllamaEdited(String oldPromptText, PromptRequest editedRequest);
    PromptRequest deletePrompt(Long id);
}
