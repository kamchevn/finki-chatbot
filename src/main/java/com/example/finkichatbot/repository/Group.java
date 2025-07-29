package com.example.finkichatbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Group extends JpaRepository<Group, Long> {
}
