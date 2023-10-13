package com.article.article.service;

import com.article.article.component.SearchResultsProducer;
import com.article.article.dto.BigkindsRequestParam;
import com.article.article.dto.BigkindsResponse;
import com.article.article.dto.CompanySearchParam;
import com.article.article.entity.Article;
import com.article.article.entity.ArticleCnt;
import com.article.article.mapper.ArticleMapper;
import com.article.article.repository.ArticleCntRepository;
import com.article.article.repository.ArticleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Service
public class BigkindsArticleService {

    @Value("${bigkinds.api.url}")
    private String bigkindsUrl;

    private static final Logger log = LoggerFactory.getLogger(BigkindsArticleService.class);

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final ArticleCntRepository articleCntRepository;
    private final RestTemplate restTemplate;
    private final SearchResultsProducer searchResultsProducer;

    public BigkindsArticleService(ArticleRepository articleRepository, ArticleMapper articleMapper, ArticleCntRepository articleCntRepository, RestTemplate restTemplate, SearchResultsProducer searchResultsProducer) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
        this.articleCntRepository = articleCntRepository;
        this.restTemplate = restTemplate;
        this.searchResultsProducer = searchResultsProducer;
    }

    // 빅카인즈 뉴스 검색로직
    @Transactional
    public BigkindsResponse searchBigkindsArticle (CompanySearchParam searchParam) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        BigkindsRequestParam requestParam = new BigkindsRequestParam(searchParam.getCompanyName() + " " + searchParam.getCeoName());

        HttpEntity<BigkindsRequestParam> requestEntity = new HttpEntity<>(requestParam, headers);

        ResponseEntity<BigkindsResponse> response = restTemplate.exchange(bigkindsUrl, HttpMethod.POST, requestEntity, BigkindsResponse.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        BigkindsResponse bigkindsResponse = response.getBody();

        // 빅카인즈 전체 기사 수 확인
        int total = bigkindsResponse.getReturnObject().getTotalHits();

        // 수집한 자료가 있는지 여부 확인
        if (bigkindsResponse != null && bigkindsResponse.getReturnObject() != null && bigkindsResponse.getReturnObject().getDocuments() != null) {
            for (BigkindsResponse.Document document : bigkindsResponse.getReturnObject().getDocuments()) {
                String title = document.getTitle();
                String originLink = document.getProviderLinkPage();
                LocalDate ymd = document.getPublishedAt().toLocalDate();

                // 기사가 있는 경우 중복검사 후 기사 수집
                if (!isDuplicateNews(title, originLink)) {

                    Article article = articleMapper.bigkindsResponseToArticle(document);
                    article.setIdSeq(searchParam.getId_seq());

                    /// 기사 발행일 일치여부 확인, 같은날 발행 된 기사는 카운트 +1
                    /*if (!isDuplicateDate(ymd)) {
                        ArticleCnt articleCnt = articleMapper.createBigKindsArticleCnt(document);
                        articleCnt.setIdSeq(searchParam.getId_seq());
                        articleCntRepository.save(articleCnt);
                    } else {
                        ArticleCnt findArticleCnt = articleCntRepository.findByArticleYMD(ymd);
                        if (findArticleCnt != null) {
                            findArticleCnt.setArticleCnt(findArticleCnt.getArticleCnt() + 1);
                            articleCntRepository.save(findArticleCnt);
                        }
                    }*/

                    // 검색 결과 큐로 전달
                    /*String searchResultJson = objectMapper.writeValueAsString(article);
                    searchResultsProducer.sendSearchResults(searchResultJson);*/

                    articleRepository.save(article);

                }
            } log.info("빅카인즈 기사 수집 총 {}건 검색 되었습니다.", total);
        }
            return null;
    }


    // 기사 중복 검사
    private boolean isDuplicateNews(String title, String originLink) {
        return articleRepository.existsByTitleOrOriginLink(title, originLink);
    }

    // 날짜 중복 검사

}
