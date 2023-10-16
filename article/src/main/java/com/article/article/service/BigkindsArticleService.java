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

        // api 호출
        ResponseEntity<BigkindsResponse> response = restTemplate.exchange(bigkindsUrl, HttpMethod.POST, requestEntity, BigkindsResponse.class);
        BigkindsResponse bigkindsResponse = response.getBody();

        // 빅카인즈 전체 기사 수 확인
        int total = bigkindsResponse.getReturnObject().getTotalHits();

        // 원활한 큐 처리를 위해 데이터를 List에 담아서 전달
        List<Article> articleList = new ArrayList<>();
        List<ArticleCnt> articleCntList = new ArrayList<>();

        if (bigkindsResponse != null && bigkindsResponse.getReturnObject() != null && bigkindsResponse.getReturnObject().getDocuments() != null) {
            // 제목과 링크 중복되지 않은 항목만 필터링
            List<BigkindsResponse.Document> nonDuplicateDocument = Arrays.stream(bigkindsResponse.getReturnObject().getDocuments())
                    .filter(document -> !isDuplicateNews(document.getTitle(), document.getProviderLinkPage()))
                    .collect(Collectors.toList());

            for (BigkindsResponse.Document document : nonDuplicateDocument) {
                LocalDate date = document.getPublishedAt().toLocalDate();

                // 수집한 항목에서 같은 날짜에 발행 된 기사가 있는지 확인
                ArticleCnt existingArticleCnt = articleCntList.stream()
                        .filter(articleCnt -> articleCnt.getArticleYMD().isEqual(date))
                        .findFirst()
                        .orElse(null);

                // 수집한 기사를 리스트에 추가
                Article sendBigkindsArticle = articleMapper.bigkindsResponseToArticle(document, searchParam);
                articleList.add(sendBigkindsArticle);

                if (existingArticleCnt == null) {
                    // 수집한 목록과 DB를 모두 체크했을 때 최초 기사일 경우 신규 데이터 생성
                    if (!isDuplicateArticleCnt(date)) {
                        ArticleCnt articleCnt = articleMapper.searchArticleCnt(sendBigkindsArticle, searchParam);
                        articleCntList.add(articleCnt);
                    // 수집한 목록에는 신규 날짜지만 기존 DB에 이미 기사가 발행 된 이력이 있을 경우 기존 데이터 cnt 컬럼에 카운트 +1
                    } else {
                        ArticleCnt articleCnt = articleCntRepository.findByArticleYMD(date);
                        articleCnt.setArticleCnt(articleCnt.getArticleCnt() + 1);
                        articleCntRepository.save(articleCnt);
                    }
                // 같은 날짜에 발행된 다른 기사라면 cnt 컬럼에 +1
                } else {
                    existingArticleCnt.setArticleCnt(existingArticleCnt.getArticleCnt() + 1);
                }

            }
            // 수집 결과를 큐로 보내준 후 리스트 초기화
            searchResultsProducer.sendSearchResults(articleList);
            searchResultsProducer.sendArticleCntResult(articleCntList);
            articleList.clear();
            articleCntList.clear();
            log.info("빅카인즈 기사 수집 총 {}건 검색 되었습니다.", total);
        }
        return null;
    }


    // 기사 중복 검사
    private boolean isDuplicateNews(String title, String originLink) {
        return articleRepository.existsByTitleOrOriginLink(title, originLink);
    }

    // 날짜 중복 검사
    private boolean isDuplicateArticleCnt(LocalDate articleYMD) {
        return articleCntRepository.existsByArticleYMD(articleYMD);
    }

}
