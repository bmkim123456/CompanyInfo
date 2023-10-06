package com.article.article.component;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SearchResultsProducer {

    private final AmqpTemplate rabbitTemplate;

    public SearchResultsProducer(AmqpTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    // 큐로 저장할 뉴스 전달
    public void sendSearchResults(String searchResultJson) {
        rabbitTemplate.convertAndSend("article-result", searchResultJson);
    }
}
