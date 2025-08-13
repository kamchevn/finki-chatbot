package com.example.finkichatbot.service.impl;

import com.example.finkichatbot.model.Chat;
import com.example.finkichatbot.model.Log;
import com.example.finkichatbot.model.PromptRequest;
import com.example.finkichatbot.model.PromptResponse;
import com.example.finkichatbot.repository.LogRepository;
import com.example.finkichatbot.repository.PromptRequestRepository;
import com.example.finkichatbot.repository.PromptResponseRepository;
import com.example.finkichatbot.service.ChatService;
import com.example.finkichatbot.service.LogService;
import com.example.finkichatbot.service.PromptService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PromptServiceImpl implements PromptService {

    private final RestTemplate restTemplate;
    @Value("${llama.api.url}")
    private String llamaApiUrl;
    private final PromptRequestRepository promptRequestRepository;
    private final PromptResponseRepository promptResponseRepository;
    private final LogService logService;
    private final ChatService chatService;

    public PromptServiceImpl(RestTemplate restTemplate, PromptRequestRepository promptRequestRepository, PromptResponseRepository promptResponseRepository, LogService logService, ChatService chatService) {
        this.restTemplate = restTemplate;
        this.promptRequestRepository = promptRequestRepository;
        this.promptResponseRepository = promptResponseRepository;
        this.logService = logService;
        this.chatService = chatService;
    }

    @Override
    public PromptRequest findByPrompt(String prompt) {
        return promptRequestRepository.findByPrompt(prompt).orElseThrow(() -> new RuntimeException("Prompt not found"));
    }

    @Override
    public List<PromptRequest> getRequestsFromChat(Long chatId) {
        Chat chat = chatService.getChat(chatId);
        return promptRequestRepository.findAllByChat(chat);
    }

    @Override
    public List<PromptResponse> getResponsesFromChat(Long chatId) {
        Chat chat = chatService.getChat(chatId);
        return promptResponseRepository.findAllByChat(chat);
    }

    @Override
    public PromptRequest createPrompt(String prompt, Long chatId) {
        Chat chat = chatService.getChat(chatId);
        PromptRequest promptRequest = new PromptRequest(prompt,"llama3.3:70b");
        promptRequest.setChat(chat);
        return promptRequestRepository.save(promptRequest);
    }

    @Override
    public PromptResponse askOllama(PromptRequest promptRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PromptRequest> entity = new HttpEntity<>(promptRequest, headers);

        ResponseEntity<PromptResponse> response =
                restTemplate.postForEntity(llamaApiUrl, entity, PromptResponse.class);
        String responseText = response.getBody().getResponse();
        PromptResponse promptResponse = new PromptResponse(responseText);
        promptResponse.setChat(promptRequest.getChat());
        promptResponseRepository.save(promptResponse);
        logService.createLog(promptRequest,promptResponse);
        return promptResponse;
    }

    @Override
    public PromptRequest editPrompt(String oldPrompt, String newPrompt, Long chatId) {
        PromptRequest request = promptRequestRepository.findByPrompt(oldPrompt).orElseThrow(() -> new RuntimeException("Prompt not found"));
        Log log = logService.findLogByPromptRequest(request);
        PromptResponse toDelete = log.getResponse();
        promptResponseRepository.delete(toDelete);
        request.setPrompt(newPrompt);
        promptRequestRepository.save(request);
        return request;
    }

    @Override
    public PromptResponse askOllamaEdited(String oldPromptText, PromptRequest editedRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PromptRequest> entity = new HttpEntity<>(editedRequest, headers);

        ResponseEntity<PromptResponse> response =
                restTemplate.postForEntity(llamaApiUrl, entity, PromptResponse.class);
        String responseText = response.getBody().getResponse();
        PromptResponse promptResponse = new PromptResponse(responseText);
        promptResponse.setChat(editedRequest.getChat());
        promptResponseRepository.save(promptResponse);
        logService.editLog(oldPromptText,editedRequest,promptResponse);
        return promptResponse;
    }

    @Override
    public PromptRequest deletePrompt(Long id) {
        PromptRequest request = promptRequestRepository.findById(id).orElseThrow(() -> new RuntimeException("Prompt not found"));
        Log log = logService.findLogByPromptRequest(request);
        PromptResponse response = log.getResponse();
        logService.deleteLog(request);
        promptRequestRepository.delete(request);
        promptResponseRepository.delete(response);
        return request;
    }
}
