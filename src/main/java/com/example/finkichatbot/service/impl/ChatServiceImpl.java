package com.example.finkichatbot.service.impl;

import com.example.finkichatbot.model.Chat;
import com.example.finkichatbot.model.PromptRequest;
import com.example.finkichatbot.model.PromptResponse;
import com.example.finkichatbot.repository.ChatRepository;
import com.example.finkichatbot.repository.PromptRequestRepository;
import com.example.finkichatbot.repository.PromptResponseRepository;
import com.example.finkichatbot.service.ChatService;
import com.example.finkichatbot.service.LogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final PromptRequestRepository promptRequestRepository;
    private final PromptResponseRepository promptResponseRepository;
    private final LogService logService;

    public ChatServiceImpl(ChatRepository chatRepository, PromptRequestRepository promptRequestRepository, PromptResponseRepository promptResponseRepository, LogService logService) {
        this.chatRepository = chatRepository;
        this.promptRequestRepository = promptRequestRepository;
        this.promptResponseRepository = promptResponseRepository;
        this.logService = logService;
    }

    @Override
    public Chat getChat(Long id) {
        return chatRepository.findById(id).orElseThrow(() -> new RuntimeException("Chat with id " + id + "not found"));
    }

    @Override
    public Chat createChat(String chatName) {
        return chatRepository.save(new Chat(chatName));
    }

    @Override
    public Chat editChat(Long id, String chatName) {
        Chat chat = this.getChat(id);
        chat.setChatName(chatName);
        return chatRepository.save(chat);
    }

    @Override
    public Chat deleteChat(Long id) {
        Chat chat = this.getChat(id);
        List<PromptRequest> requests = promptRequestRepository.findAllByChat(chat);
        List<PromptResponse> responses = promptResponseRepository.findAllByChat(chat);
        for(PromptRequest request : requests){
            logService.deleteLog(request);
        }
        for(PromptRequest request : requests){
            request.setChat(null);
        }
        for(PromptResponse response : responses){
            response.setChat(null);
        }
        chatRepository.delete(chat);
        promptRequestRepository.deleteAll(requests);
        promptResponseRepository.deleteAll(responses);
        return chat;
    }
}
