package com.example.finkichatbot.repository;

import com.example.finkichatbot.model.Chat;
import com.example.finkichatbot.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat,Long> {
    List<Chat> findAllByGroup(Group group);
}
