package com.article.article.service;

import com.article.article.component.SearchResultsProducer;
import com.article.article.dto.CompanySearchParam;
import com.article.article.dto.NaverResponse;
import com.article.article.entity.Article;
import com.article.article.entity.ArticleCnt;
import com.article.article.mapper.ArticleMapper;
import com.article.article.repository.ArticleCntRepository;
import com.article.article.repository.ArticleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.databind.util.JSONWrappedObject;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class NaverArticleService {

    private static final Logger log = LoggerFactory.getLogger(NaverArticleService.class);

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final ArticleCntRepository articleCntRepository;
    private final RestTemplate restTemplate;
    private final SearchResultsProducer searchResultsProducer;

    public NaverArticleService(ArticleRepository articleRepository, ArticleMapper articleMapper, ArticleCntRepository articleCntRepository, RestTemplate restTemplate, SearchResultsProducer searchResultsProducer) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
        this.articleCntRepository = articleCntRepository;
        this.restTemplate = restTemplate;
        this.searchResultsProducer = searchResultsProducer;
    }

    // 네이버 api키 사용횟수 카운트 초기화
    AtomicLong KEY_COUNT = new AtomicLong(1);

    // 네이버 뉴스기사 검새 로직
    public NaverResponse searchNaverArticles (CompanySearchParam searchParam) throws InterruptedException {

        int start = 1;

        while (true) {
            String encodedKeyword = searchParam.getCompanyName() + " " + searchParam.getCeoName();

            log.info(encodedKeyword);

            String apiUrl = "https://openapi.naver.com/v1/search/news.json?query=" + encodedKeyword + "&start=" + start + "&display=100";
            log.info("키 사용 횟수 : {}", KEY_COUNT.get());
            KEY_COUNT.incrementAndGet();
            start += 100;

            // api 키값 설정
            HttpHeaders headers = createRequestHeaders();

            // API 호출
            ResponseEntity<NaverResponse> response = restTemplate.exchange(apiUrl, HttpMethod.GET, new HttpEntity<>(headers), NaverResponse.class);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            NaverResponse naverResponse = response.getBody();

            List<Article> naverArticle = new ArrayList<>();
            List<ArticleCnt> articleCntList = new ArrayList<>();

            int total = naverResponse.getTotal();

            if (naverResponse != null && naverResponse.getItems() != null) {
                // 중복되지 않은 항목만 필터링
                List<NaverResponse.Items> nonDuplicateItems = Arrays.stream(naverResponse.getItems())
                        .filter(items -> !isDuplicateNews(items.getTitle(), items.getOriginalLink()))
                        .collect(Collectors.toList());

                for (NaverResponse.Items items : nonDuplicateItems) {
                    LocalDate date = items.getPubDate().toLocalDate();

                    // 기사 발행일에 따라 ArticleCnt를 찾거나 추가
                    ArticleCnt existingArticleCnt = articleCntList.stream()
                            .filter(articleCnt -> articleCnt.getArticleYMD().isEqual(date))
                            .findFirst()
                            .orElse(null);

                    Article sendNaverArticle = articleMapper.naverResponseToArticle(items, searchParam);
                    naverArticle.add(sendNaverArticle);

                    if (existingArticleCnt == null) {
                        ArticleCnt articleCnt = articleMapper.searchArticleCnt(sendNaverArticle, searchParam);
                        articleCntList.add(articleCnt);
                    } else {
                        existingArticleCnt.setArticleCnt(existingArticleCnt.getArticleCnt() + 1);
                    }
                }

                // 각각 큐로 전달
                searchResultsProducer.sendSearchResults(naverArticle);
                searchResultsProducer.sendArticleCntResult(articleCntList);

                naverArticle.clear();
                articleCntList.clear();
            }


            if (start > 100 || start > total) {
                log.info("네이버 기사 총 {}건 검색 되었습니다.", total);
                break;
            }

        }
        return null;
    }


    // 뉴스 중복 조회
    private boolean isDuplicateNews(String title, String originLink) {
        return articleRepository.existsByTitleOrOriginLink(title, originLink);
    }


    // 네이버 api 키 전달
    private HttpHeaders createRequestHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        if (KEY_COUNT.intValue() <= 25000) {
            headers.set("X-Naver-Client-Id", "N97AIPxC798NeqYQQBDv");
            headers.set("X-Naver-Client-Secret", "cXcIIQ78M8");
        } else if (KEY_COUNT.intValue() <= 50000) {
            headers.set("X-Naver-Client-Id", "4YiTysikkf5h22QoZZB7");
            headers.set("X-Naver-Client-Secret", "IAjxIy2K0n");
        }
        return headers;
    }
}
