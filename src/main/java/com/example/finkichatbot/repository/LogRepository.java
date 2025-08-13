package com.example.finkichatbot.repository;

import com.example.finkichatbot.model.Log;
import com.example.finkichatbot.model.PromptRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LogRepository extends JpaRepository<Log,Long> {
    Optional<Log> findByPrompt(PromptRequest promptRequest);
}
