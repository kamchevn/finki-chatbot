package com.example.finkichatbot.web;

import com.example.finkichatbot.model.Chat;
import com.example.finkichatbot.model.Log;
import com.example.finkichatbot.model.PromptRequest;
import com.example.finkichatbot.model.PromptResponse;
import com.example.finkichatbot.model.dto.LogDto;
import com.example.finkichatbot.model.dto.PromptRequestDto;
import com.example.finkichatbot.model.dto.PromptResponseDto;
import com.example.finkichatbot.service.ChatService;
import com.example.finkichatbot.service.GroupService;
import com.example.finkichatbot.service.LogService;
import com.example.finkichatbot.service.PromptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final PromptService promptService;
    private final ChatService chatService;
    private final LogService logService;
    private final GroupService groupService;

    public ChatController(PromptService promptService, ChatService chatService, LogService logService, GroupService groupService) {
        this.promptService = promptService;
        this.chatService = chatService;
        this.logService = logService;
        this.groupService = groupService;
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<List<LogDto>> loadChat(@PathVariable Long chatId){
        Chat chat = this.chatService.getChat(chatId);
        List<PromptRequest> chatRequests = promptService.getRequestsFromChat(chatId);
        List<LogDto> logs = new ArrayList<>();
        for(PromptRequest request : chatRequests){
            Log log = logService.findLogByPromptRequest(request);
            PromptResponse response = log.getResponse();
            PromptRequestDto requestDto = new PromptRequestDto(request.getModel(),request.getPrompt(), request.isStream(), chatId);
            PromptResponseDto responseDto = new PromptResponseDto(response.getResponse());
            LogDto dto = new LogDto(requestDto,responseDto,log.getPromptEntryTime());
            logs.add(dto);
        }
        return ResponseEntity.ok(logs);
    }

    @PostMapping("/edit/{chatId}")
    public ResponseEntity<Chat> editChatName(@PathVariable Long chatId, @RequestParam String chatName){
        if(chatService.getChat(chatId) != null){
            return ResponseEntity.ok(chatService.editChat(chatId,chatName));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/delete/{chatId}")
    public ResponseEntity<Chat> deleteChat(@PathVariable Long chatId){
        if(chatService.getChat(chatId) != null){
            return ResponseEntity.ok(chatService.deleteChat(chatId));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/addToGroup/{groupId}")
    public ResponseEntity<String> addChatToGroup(@PathVariable Long groupId, @RequestParam Long chatId){
        if(chatService.getChat(chatId) != null && groupService.getGroup(groupId) != null){
            groupService.addChatToGroup(groupId,chatId);
            return ResponseEntity.ok("Chat added to group successfully!");
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping("/removeFromGroup/{groupId}")
    public ResponseEntity<String> removeChatToGroup(@PathVariable Long groupId, @RequestParam Long chatId){
        if(chatService.getChat(chatId) != null && groupService.getGroup(groupId) != null){
            groupService.removeChatFromGroup(groupId,chatId);
            return ResponseEntity.ok("Chat removed from group successfully!");
        }
        return ResponseEntity.notFound().build();
    }
}
