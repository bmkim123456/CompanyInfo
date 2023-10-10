package com.article.article.component;

import com.article.article.dto.BigkindsResponse;
import com.article.article.dto.CompanySearchParam;
import com.article.article.entity.Article;
import com.article.article.repository.ArticleRepository;
import com.article.article.service.ArticleService;
import com.article.article.service.BigkindsArticleService;
import com.article.article.service.NaverArticleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SearchResultsConsumer {

    private static final Logger log = LoggerFactory.getLogger(SearchResultsConsumer.class);
    private final ArticleRepository articleRepository;
    private final ArticleService articleService;
    private final ObjectMapper objectMapper;

    public SearchResultsConsumer(ArticleRepository articleRepository, ArticleService articleService, ObjectMapper objectMapper) {
        this.articleRepository = articleRepository;
        this.articleService = articleService;
        this.objectMapper = objectMapper;
    }

    // 큐에 저장된 기업 정보를 가져옴
    @RabbitListener(queues = "company-info", concurrency = "1")
    public void receiveCompanyInfo (String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CompanySearchParam searchParam = objectMapper.readValue(message, CompanySearchParam.class);

            articleService.articleSearch(searchParam);

            Thread.sleep(100);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new RuntimeException("전달 실패", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // 큐에 저장된 뉴스기사를 n개씩 가져와서 db에 저장
    @RabbitListener(queues = "article-result", concurrency = "10")
    public void receiveSearchResults(String searchResultJson) {
        try {
            // JSON 형태의 검색 결과를 가져오기
            Article article = objectMapper.readValue(searchResultJson, Article.class);

            // 가져온 JSON 데이터 저장
            Article news = createArticleEntity(article);
            articleRepository.save(news);

            Thread.sleep(500);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new RuntimeException("저장 실패", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private Article createArticleEntity(Article article) {
        Article news = new Article();
        news.setIdSeq(article.getIdSeq());
        news.setSource(article.getSource());
        news.setCreateDatetime(article.getCreateDatetime());
        news.setTitle(article.getTitle());
        news.setOriginLink(article.getOriginLink());
        news.setLink(article.getLink());
        news.setPrevContent(article.getPrevContent());
        news.setUpdateDatetime(article.getUpdateDatetime());
        news.setPublishDatetime(article.getPublishDatetime());
        news.setNewsId(article.getNewsId());
        news.setPublisher(article.getPublisher());
        news.setAuthor(article.getAuthor());
        return news;
    }
}
