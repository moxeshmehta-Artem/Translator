package com.translator.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.translator.model.CanonicalPatient;
import org.springframework.stereotype.Service;

@Service
public class JsonParserService {
    private final ObjectMapper objectMapper;

    public JsonParserService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        this.objectMapper.configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
                false);
    }

    public CanonicalPatient parse(String payload) {
        try {
            return objectMapper.readValue(payload, CanonicalPatient.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON patient", e);
        }
    }
}
