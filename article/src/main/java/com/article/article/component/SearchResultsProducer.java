package com.article.article.component;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SearchResultsProducer {

    @Value("${rabbitmq.queueName}")
    private String queueName;

    private final AmqpTemplate rabbitTemplate;

    public SearchResultsProducer(AmqpTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendSearchResults(String searchResultJson) {
        rabbitTemplate.convertAndSend(queueName, searchResultJson);
    }
}
