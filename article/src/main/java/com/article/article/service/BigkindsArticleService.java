package com.article.article.service;

import com.article.article.dto.BigkindsRequestParam;
import com.article.article.dto.BigkindsResponse;
import com.article.article.dto.CompanySearchParam;
import com.article.article.entity.Article;
import com.article.article.mapper.ArticleMapper;
import com.article.article.repository.ArticleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class BigkindsArticleService {

    @Value("${bigkinds.api.url}")
    private String bigkindsUrl;

    private static final Logger log = LoggerFactory.getLogger(BigkindsArticleService.class);

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final RestTemplate restTemplate;

    public BigkindsArticleService(ArticleRepository articleRepository, ArticleMapper articleMapper, RestTemplate restTemplate) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
        this.restTemplate = restTemplate;
    }

    // 빅카인즈 뉴스 검색로직
    @Transactional
    public List<Article> searchBigkindsArticle (CompanySearchParam searchParam) throws JsonProcessingException {
        List<Article> articleList = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        BigkindsRequestParam requestParam = new BigkindsRequestParam(searchParam.getCompanyName() + " " + searchParam.getCeoName());

        HttpEntity<BigkindsRequestParam> requestEntity = new HttpEntity<>(requestParam, headers);

        // api 호출
        ResponseEntity<BigkindsResponse> response = restTemplate.exchange(bigkindsUrl, HttpMethod.POST, requestEntity, BigkindsResponse.class);
        BigkindsResponse bigkindsResponse = response.getBody();

        // 빅카인즈 전체 기사 수 확인
        int total = bigkindsResponse.getReturnObject().getTotalHits();

        if (bigkindsResponse != null && bigkindsResponse.getReturnObject() != null && bigkindsResponse.getReturnObject().getDocuments() != null) {
            // 제목과 링크 중복되지 않은 항목만 필터링
            List<BigkindsResponse.Document> nonDuplicateDocument = Arrays.stream(bigkindsResponse.getReturnObject().getDocuments())
                    .filter(document -> !isDuplicateNews(document.getTitle(), document.getProviderLinkPage()))
                    .collect(Collectors.toList());

            for (BigkindsResponse.Document document : nonDuplicateDocument) {

                // 수집한 기사를 리스트에 추가
                Article sendBigkindsArticle = articleMapper.bigKindsResponseToArticle(document, searchParam);
                articleList.add(sendBigkindsArticle);

            }

            log.info("빅카인즈 기사 수집 총 {}건 검색 되었습니다.", total);
        }
        return articleList;
    }


    // 기사 중복 검사
    private boolean isDuplicateNews(String title, String originLink) {
        return articleRepository.existsByTitleAndOriginLink(title, originLink);
    }


}
