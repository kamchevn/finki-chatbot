package com.example.finkichatbot.repository;

import com.example.finkichatbot.model.Chat;
import com.example.finkichatbot.model.PromptRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromptRequestRepository extends JpaRepository<PromptRequest,Long> {
    List<PromptRequest> findAllByChat(Chat chat);
    Optional<PromptRequest> findByPrompt(String prompt);
}
