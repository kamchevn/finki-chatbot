package com.example.finkichatbot.web;

import com.example.finkichatbot.model.dto.GlossarySearchResult;
import com.example.finkichatbot.service.impl.GlossaryServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/glossary")
@CrossOrigin("*")
public class GlossaryController {

    private final GlossaryServiceImpl service;

    public GlossaryController(GlossaryServiceImpl service) {
        this.service = service;
    }

    // GET /glossary/search?q=roof leak&k=5
    @GetMapping("/search")
    public List<GlossarySearchResult> search(
            @RequestParam("q") String q,
            @RequestParam(name = "k", defaultValue = "5") int k
    ) {
        return service.search(q, k);
    }
}
