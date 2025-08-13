package com.example.finkichatbot.service;

import com.example.finkichatbot.model.Log;
import com.example.finkichatbot.model.PromptRequest;
import com.example.finkichatbot.model.PromptResponse;

public interface LogService {
    Log findLogByPromptRequest(PromptRequest promptRequest);
    Log createLog(PromptRequest promptRequest, PromptResponse promptResponse);
    Log editLog(String oldPrompt, PromptRequest newPrompt, PromptResponse promptResponse);
    Log deleteLog(PromptRequest prompt);
}
