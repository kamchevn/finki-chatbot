package com.example.finkichatbot.service.impl;

import com.example.finkichatbot.model.Log;
import com.example.finkichatbot.model.PromptRequest;
import com.example.finkichatbot.model.PromptResponse;
import com.example.finkichatbot.repository.LogRepository;
import com.example.finkichatbot.repository.PromptRequestRepository;
import com.example.finkichatbot.service.LogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LogServiceImpl implements LogService {
    private final LogRepository logRepository;
    private final PromptRequestRepository promptRequestRepository;

    public LogServiceImpl(LogRepository logRepository, PromptRequestRepository promptRequestRepository) {
        this.logRepository = logRepository;
        this.promptRequestRepository = promptRequestRepository;
    }

    @Override
    public Log findLogByPromptRequest(PromptRequest promptRequest) {
        return logRepository.findByPrompt(promptRequest).orElseThrow(() -> new RuntimeException("Log not found"));
    }

    @Override
    public Log createLog(PromptRequest promptRequest, PromptResponse promptResponse) {
        return logRepository.save(new Log(promptRequest,promptResponse));
    }

    @Override
    public Log editLog(String oldPromptText, PromptRequest newPrompt, PromptResponse promptResponse){
        Optional<PromptRequest> oldPrompt = promptRequestRepository.findByPrompt(oldPromptText);
        if(oldPrompt.isEmpty()){
            throw new RuntimeException("Prompt not found.");
        }
        Log log = this.findLogByPromptRequest(oldPrompt.get());
        log.setPrompt(newPrompt);
        log.setResponse(promptResponse);
        log.setPromptEntryTime(LocalDateTime.now());
        return logRepository.save(log);
    }

    @Override
    public Log deleteLog(PromptRequest prompt) {
        Log log = this.findLogByPromptRequest(prompt);
        log.setPrompt(null);
        log.setResponse(null);
        logRepository.delete(log);
        return log;
    }
}
