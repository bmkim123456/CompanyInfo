package com.article.article.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class JacksonJsonMapper implements JsonMapper{

    private final ObjectMapper objectMapper;

    public JacksonJsonMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public JsonNode readTree(String json) throws JsonProcessingException {
        return objectMapper.readTree(json);
    }
}
