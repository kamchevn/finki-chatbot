package com.example.finkichatbot.repository;

import com.example.finkichatbot.model.Chat;
import com.example.finkichatbot.model.Prompt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromptRepository extends JpaRepository<Prompt,Long> {
    List<Prompt> findAllByChat(Chat chat);
}
