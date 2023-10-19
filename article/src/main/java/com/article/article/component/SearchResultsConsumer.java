package com.article.article.component;

import com.article.article.dto.CompanySearchParam;
import com.article.article.entity.Article;
import com.article.article.entity.ArticleCnt;
import com.article.article.mapper.ArticleMapper;
import com.article.article.repository.ArticleCntRepository;
import com.article.article.repository.ArticleRepository;
import com.article.article.service.ArticleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchResultsConsumer {

    private static final Logger log = LoggerFactory.getLogger(SearchResultsConsumer.class);
    private final ArticleRepository articleRepository;
    private final ArticleCntRepository articleCntRepository;
    private final ArticleService articleService;
    private final ArticleMapper articleMapper;

    public SearchResultsConsumer(ArticleRepository articleRepository, ArticleCntRepository articleCntRepository, ArticleService articleService, ArticleMapper articleMapper) {
        this.articleRepository = articleRepository;
        this.articleCntRepository = articleCntRepository;
        this.articleService = articleService;
        this.articleMapper = articleMapper;
    }

    // 큐에 저장된 기업 정보를 가져옴
    @RabbitListener(queues = "hubble.article.queue", concurrency = "1")
    public void receiveCompanyInfo (String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CompanySearchParam searchParam = objectMapper.readValue(message, CompanySearchParam.class);

            articleService.articleSearch(searchParam);

            Thread.sleep(1000);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new RuntimeException("전달 실패", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /*// 큐에 저장된 뉴스기사를 n개씩 가져와서 db에 저장
    @RabbitListener(queues = "article-result", concurrency = "1")
    public void receiveSearchResults(List<Article> articles) {
        try {
            // articles 목록을 직젬화한 데이터를 사용
            for (Article article : articles) {
                // 가져온 JSON 데이터 저장
                Article news = articleMapper.createArticle(article);
                articleRepository.save(news);
            }

            Thread.sleep(500);
        }  catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @RabbitListener(queues = "articleCnt-result", concurrency = "1")
    public void receiveArticleCntResults(List<ArticleCnt> articleCntList) {
        try {
            // articles 목록을 직젬화한 데이터를 사용
            for (ArticleCnt articleCnt : articleCntList) {
                // 가져온 JSON 데이터 저장
                ArticleCnt articleCntResult = articleMapper.articleCntResult(articleCnt);
                articleCntRepository.save(articleCntResult);
            }

            Thread.sleep(500);
        }  catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }*/

}
