package com.example.finkichatbot.service;

import com.example.finkichatbot.model.Group;

public interface GroupService {
    Group getGroup(Long id);
    Group createGroup(String groupName);
    Group editGroup(Long id, String groupName);
    Group deleteGroup(Long id);
    void addChatToGroup(Long id, Long chatId);
    void removeChatFromGroup(Long id, Long chatId);
}
