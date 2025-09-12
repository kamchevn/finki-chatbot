package com.example.finkichatbot.web;
import com.example.finkichatbot.service.impl.GlossaryCsvImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/import")
public class GlossaryImportController {
    private final GlossaryCsvImportService service;
    public GlossaryImportController(GlossaryCsvImportService service) { this.service = service; }

    @PostMapping("/glossary-csv")
    public ResponseEntity<String> importCsv(@RequestParam("file") MultipartFile file) throws Exception {
        long rows = service.importCsv(file);
        return ResponseEntity.ok("Imported rows: " + rows);
    }
}
