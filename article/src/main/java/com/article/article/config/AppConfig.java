package com.article.article.config;

import com.article.article.dto.BigkindsResponse;
import com.article.article.dto.NaverResponse;
import com.article.article.entity.Article;
import com.article.article.mapper.ArticleMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ArticleMapper articleMapper() {
        return new ArticleMapper() {
            @Override
            public Article naverResponseToArticle(NaverResponse.Items items) {
                return ArticleMapper.super.naverResponseToArticle(items);
            }

            @Override
            public Article bigkindsResponseToArticle(BigkindsResponse.Document response) {
                return ArticleMapper.super.bigkindsResponseToArticle(response);
            }

            @Override
            public Article createArticle(Article article) {
                return ArticleMapper.super.createArticle(article);
            }
        };
    }
}
