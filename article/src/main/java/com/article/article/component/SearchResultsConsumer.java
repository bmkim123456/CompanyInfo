package com.article.article.component;

import com.article.article.dto.CompanySearchParam;
import com.article.article.entity.Naver;
import com.article.article.repository.NaverRepository;
import com.article.article.service.NaverArticleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SearchResultsConsumer {

    private static final Logger log = LoggerFactory.getLogger(SearchResultsConsumer.class);
    private final NaverRepository naverRepository;
    private final NaverArticleService naverArticleService;
    private final ObjectMapper objectMapper;

    public SearchResultsConsumer(NaverRepository naverRepository, NaverArticleService naverArticleService, ObjectMapper objectMapper) {
        this.naverRepository = naverRepository;
        this.naverArticleService = naverArticleService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "company-info", concurrency = "1")
    public void receiveCompanyInfo (String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CompanySearchParam searchParam = objectMapper.readValue(message, CompanySearchParam.class);

            String result = naverArticleService.searchArticle(searchParam);
            log.info(result);

            Thread.sleep(500);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new RuntimeException("전달 실패", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @RabbitListener(queues = "${rabbitmq.queueName}", concurrency = "1")
    public void receiveSearchResults(String searchResultJson) {
        try {
            // JSON 형태의 검색 결과를 가져오기
            Naver naver = objectMapper.readValue(searchResultJson, Naver.class);

            // 가져온 JSON 데이터 저장
            Naver news = createNaverEntity(naver);
            naverRepository.save(news);

            Thread.sleep(500);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new RuntimeException("저장 실패", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private Naver createNaverEntity(Naver naver) {
        Naver news = new Naver();
        news.setId_seq(naver.getId_seq());
        news.setSource(naver.getSource());
        news.setCreate_datetime(naver.getCreate_datetime());
        news.setTitle(naver.getTitle());
        news.setOriginLink(naver.getOriginLink());
        news.setLink(naver.getLink());
        news.setPrev_content(naver.getPrev_content());
        news.setUpdate_datetime(naver.getUpdate_datetime());
        return news;
    }
}
