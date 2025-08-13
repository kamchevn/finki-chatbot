package com.example.finkichatbot.service.impl;

import com.example.finkichatbot.model.Chat;
import com.example.finkichatbot.model.Group;
import com.example.finkichatbot.repository.ChatRepository;
import com.example.finkichatbot.repository.GroupRepository;
import com.example.finkichatbot.service.ChatService;
import com.example.finkichatbot.service.GroupService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final ChatService chatService;
    private final ChatRepository chatRepository;

    public GroupServiceImpl(GroupRepository groupRepository, ChatService chatService, ChatRepository chatRepository) {
        this.groupRepository = groupRepository;
        this.chatService = chatService;
        this.chatRepository = chatRepository;
    }

    @Override
    public Group getGroup(Long id) {
        return groupRepository.findById(id).orElseThrow(() -> new RuntimeException("Group with id " + id + " not found."));
    }

    @Override
    public Group createGroup(String groupName) {
        return groupRepository.save(new Group(groupName));
    }

    @Override
    public Group editGroup(Long id, String groupName) {
        Group group = this.getGroup(id);
        group.setGroupName(groupName);
        return groupRepository.save(group);
    }

    @Override
    public Group deleteGroup(Long id) {
        Group group = this.getGroup(id);
        List<Chat> chats = chatRepository.findAllByGroup(group);
        for(Chat chat : chats){
            chat.setGroup(null);
            chatRepository.save(chat);
        }
        groupRepository.delete(group);
        return null;
    }

    @Override
    public void addChatToGroup(Long id, Long chatId) {
        Group group = this.getGroup(id);
        Chat chat = chatService.getChat(chatId);
        chat.setGroup(group);
        chatRepository.save(chat);
        group.getChats().add(chat);
        groupRepository.save(group);
    }

    @Override
    public void removeChatFromGroup(Long id, Long chatId) {
        Group group = this.getGroup(id);
        Chat chat = chatService.getChat(chatId);
        chat.setGroup(null);
        chatRepository.save(chat);
        group.getChats().remove(chat);
        groupRepository.save(group);
    }
}
