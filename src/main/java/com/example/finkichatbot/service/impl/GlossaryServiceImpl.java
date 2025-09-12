package com.example.finkichatbot.service.impl;
import com.example.finkichatbot.model.dto.GlossarySearchResult;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GlossaryServiceImpl {

    private final JdbcTemplate jdbc;

    public GlossaryServiceImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * SELECT id, content, similarity(content, :q) AS score
     * FROM glossary
     * WHERE content ILIKE '%:q%' OR name ILIKE '%:q%'
     * ORDER BY score DESC
     * LIMIT :k
     */
    public List<GlossarySearchResult> search(String query, int topK) {
        final String sql = """
        SELECT id,
               content,
               similarity(content, ?) AS score
        FROM glossary
        WHERE content ILIKE ? OR name ILIKE ?
        ORDER BY score DESC
        LIMIT ?
        """;

        String like = "%" + query + "%";
        return jdbc.query(
                sql,
                ps -> {
                    ps.setString(1, query); // for similarity(content, ?)
                    ps.setString(2, like);  // content ILIKE ?
                    ps.setString(3, like);  // name ILIKE ?
                    ps.setInt(4, topK);     // LIMIT ?
                },
                (rs, i) -> new GlossarySearchResult(
                        rs.getLong("id"),
                        rs.getString("content"),
                        rs.getDouble("score")
                )
        );
    }
}