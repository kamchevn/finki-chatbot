package com.example.finkichatbot.service;

import com.example.finkichatbot.model.Chat;

public interface ChatService {
    Chat getChat(Long id);
    Chat createChat(String chatName);
    Chat editChat(Long id, String chatName);
    Chat deleteChat(Long id);
}
