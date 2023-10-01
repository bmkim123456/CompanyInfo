package com.article.article.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

public interface JsonMapper {
    JsonNode readTree(String json) throws JsonProcessingException;
}
