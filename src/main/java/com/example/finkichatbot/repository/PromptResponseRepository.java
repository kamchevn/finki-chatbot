package com.example.finkichatbot.repository;

import com.example.finkichatbot.model.Chat;
import com.example.finkichatbot.model.PromptRequest;
import com.example.finkichatbot.model.PromptResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromptResponseRepository extends JpaRepository<PromptResponse,Long> {
    List<PromptResponse> findAllByChat(Chat chat);
}
