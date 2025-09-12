package com.example.finkichatbot.model.dto;

public record GlossarySearchResult(Long id, String content, double bm25Score) {
}
