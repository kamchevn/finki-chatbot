package com.example.finkichatbot.service.impl;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;

@Service
public class GlossaryCsvImportService {
    private final DataSource ds;
    public GlossaryCsvImportService(DataSource ds) { this.ds = ds; }

    public long importCsv(MultipartFile file) throws Exception {
        try (Connection c = ds.getConnection()) {
            CopyManager cm = new CopyManager(c.unwrap(BaseConnection.class));
            String sql = "COPY glossary (name, content) FROM STDIN WITH (FORMAT csv, HEADER true, QUOTE '\"')";
            try (Reader r = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)) {
                return cm.copyIn(sql, r);
            }
        }
    }
}
