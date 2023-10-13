package com.article.article.component;

import com.article.article.entity.Article;
import com.article.article.entity.ArticleCnt;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchResultsProducer {

    private final AmqpTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public SearchResultsProducer(AmqpTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    // 큐로 저장할 뉴스 전달
    public void sendSearchResults(List<Article> articles)  {
        rabbitTemplate.convertAndSend("article-result", articles);
    }

    public void sendArticleCntResult(List<ArticleCnt> articleCntList) {
        rabbitTemplate.convertAndSend("articleCnt-result", articleCntList);
    }
}
