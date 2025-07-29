package com.example.finkichatbot.service.impl;

import com.example.finkichatbot.model.Chat;
import com.example.finkichatbot.repository.ChatRepository;
import com.example.finkichatbot.service.ChatService;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;

    public ChatServiceImpl(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public Chat getChat(Long id) {
        return chatRepository.findById(id).orElseThrow(() -> new RuntimeException("Chat with id " + id + "not found"));
    }

    @Override
    public Chat createChat(String chatName) {
        return null;
    }

    @Override
    public Chat editChat(Long id, String chatName) {
        return null;
    }

    @Override
    public Chat deleteChat(Long id) {
        return null;
    }
}
