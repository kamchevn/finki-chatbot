package com.example.finkichatbot.service.impl;

import com.example.finkichatbot.model.Chat;
import com.example.finkichatbot.model.Prompt;
import com.example.finkichatbot.repository.LogRepository;
import com.example.finkichatbot.repository.PromptRepository;
import com.example.finkichatbot.service.ChatService;
import com.example.finkichatbot.service.PromptService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromptServiceImpl implements PromptService {
    private final PromptRepository promptRepository;
    private final LogRepository logRepository;
    private final ChatService chatService;

    public PromptServiceImpl(PromptRepository promptRepository, LogRepository logRepository, ChatService chatService) {
        this.promptRepository = promptRepository;
        this.logRepository = logRepository;
        this.chatService = chatService;
    }

    @Override
    public List<Prompt> getPromptsFromChat(Long chatId) {
        Chat chat = chatService.getChat(chatId);
        return promptRepository.findAllByChat(chat);
    }

    @Override
    public Prompt createPrompt(String prompt, String response, Long chatId) {
        return null;
    }

    @Override
    public Prompt editPrompt(Long id, String prompt, String response, Long chatId) {
        return null;
    }

    @Override
    public Prompt deletePrompt(Long id) {
        return null;
    }
}
