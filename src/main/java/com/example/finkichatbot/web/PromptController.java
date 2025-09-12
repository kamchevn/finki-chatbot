package com.example.finkichatbot.web;

import com.example.finkichatbot.model.Chat;
import com.example.finkichatbot.model.Log;
import com.example.finkichatbot.model.PromptRequest;
import com.example.finkichatbot.model.PromptResponse;
import com.example.finkichatbot.model.dto.LogDto;
import com.example.finkichatbot.model.dto.PromptRequestDto;
import com.example.finkichatbot.model.dto.PromptResponseDto;
import com.example.finkichatbot.service.ChatService;
import com.example.finkichatbot.service.LogService;
import com.example.finkichatbot.service.PromptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prompt")
@CrossOrigin("*")
public class PromptController {
    private final PromptService promptService;
    private final ChatService chatService;
    private final LogService logService;

    public PromptController(PromptService promptService, ChatService chatService, LogService logService) {
        this.promptService = promptService;
        this.chatService = chatService;
        this.logService = logService;
    }



    @PostMapping("/generate")
    public ResponseEntity<LogDto> requestPrompt(@RequestParam String promptText){
        Chat chat = this.chatService.createChat(promptText);
        PromptRequest promptRequest = promptService.createPrompt(promptText, chat.getId());
        PromptResponse promptResponse = promptService.askOllama(promptRequest);
        Log log = logService.findLogByPromptRequest(promptRequest);
        PromptRequestDto requestDto = new PromptRequestDto(promptRequest.getModel(),promptText, promptRequest.isStream(),chat.getId());
        PromptResponseDto responseDto = new PromptResponseDto(promptResponse.getResponse());
        return ResponseEntity.ok(new LogDto(requestDto,responseDto,log.getPromptEntryTime()));
    }

    @PostMapping("/generate/{chatId}")
    public ResponseEntity<LogDto> requestPromptInChat(@PathVariable Long chatId, @RequestParam String promptText){
        Chat chat = this.chatService.getChat(chatId);
        PromptRequest promptRequest = promptService.createPrompt(promptText, chat.getId());
        PromptResponse promptResponse = promptService.askOllama(promptRequest);
        Log log = logService.findLogByPromptRequest(promptRequest);
        PromptRequestDto requestDto = new PromptRequestDto(promptRequest.getModel(),promptText, promptRequest.isStream(), chatId);
        PromptResponseDto responseDto = new PromptResponseDto(promptResponse.getResponse());
        return ResponseEntity.ok(new LogDto(requestDto,responseDto,log.getPromptEntryTime()));
    }

    @PostMapping("/edit")
    public ResponseEntity<LogDto> editPrompt(@RequestParam String oldPromptText, @RequestParam String newPromptText){
        PromptRequest promptRequest = promptService.findByPrompt(oldPromptText);
        Chat chat = promptRequest.getChat();
        promptRequest = promptService.editPrompt(oldPromptText,newPromptText, chat.getId());
        PromptResponse promptResponse = promptService.askOllamaEdited(oldPromptText, promptRequest);
        Log log = logService.findLogByPromptRequest(promptRequest);
        PromptRequestDto requestDto = new PromptRequestDto(promptRequest.getModel(),newPromptText, promptRequest.isStream(), chat.getId());
        PromptResponseDto responseDto = new PromptResponseDto(promptResponse.getResponse());
        return ResponseEntity.ok(new LogDto(requestDto,responseDto,log.getPromptEntryTime()));
    }
}
