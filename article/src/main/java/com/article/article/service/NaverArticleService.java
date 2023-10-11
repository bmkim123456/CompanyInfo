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
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicLong;

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
    public NaverResponse searchNaverArticles (CompanySearchParam searchParam) throws JsonProcessingException {

        int start = 1;

        while (true) {
            String encodedKeyword = searchParam.getCompanyName() + " " + searchParam.getCeoName();

            log.info(encodedKeyword);

            String apiUrl = "https://openapi.naver.com/v1/search/news.json?query=" + encodedKeyword + "&start=" + start + "&display=100";
            log.info("키 사용 횟수 : {}", KEY_COUNT.get());
            KEY_COUNT.incrementAndGet();
            start += 1;

            // api 키값 설정
            HttpHeaders headers = createRequestHeaders();

            // API 호출
            ResponseEntity<NaverResponse> response = restTemplate.exchange(apiUrl, HttpMethod.GET, new HttpEntity<>(headers), NaverResponse.class);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            NaverResponse naverResponse = response.getBody();

            int total = naverResponse.getTotal();

            if (naverResponse != null && naverResponse.getItems() != null) {
                for (NaverResponse.Items items : naverResponse.getItems()) {
                    String title = items.getTitle();
                    String originLink = items.getOriginalLink();
                    LocalDateTime pubDate = items.getPubDate();

                    if (!(articleCntRepository.existsByArticleYMD(pubDate).isEqual(pubDate))) {
                        ArticleCnt articleCnt = new ArticleCnt();
                        articleCnt.setIdSeq(searchParam.getId_seq());
                        articleCnt.setArticleYMD(pubDate);
                        articleCnt.setArticleCnt(1);
                        articleCntRepository.save(articleCnt);
                    } else {
                        ArticleCnt findArticleCnt = articleCntRepository.findByArticleYMD(pubDate);
                        if (findArticleCnt != null) {
                            findArticleCnt.setArticleCnt(findArticleCnt.getArticleCnt() + 1);
                            articleCntRepository.save(findArticleCnt);
                        }
                    }

                    //if (isRecentNews(pubDate)) {
                    if (!isDuplicateNews(title, originLink)) {
                    Article naverArticle = articleMapper.naverResponseToArticle(items);
                    naverArticle.setIdSeq(searchParam.getId_seq());

                    // 검색 결과 큐로 전달
                        /*String searchResultJson = objectMapper.writeValueAsString(naverArticle);
                        searchResultsProducer.sendSearchResults(searchResultJson);*/

                    articleRepository.save(naverArticle);

                    }
                    //}
                }
                log.info("네이버 기사 총 {}건 검색 되었습니다.", total);
            }
            if (start > 2) {
                break;
            }
        }
        return null;
    }


    // 뉴스 중복 조회
    private boolean isDuplicateNews(String title, String originLink) {
        return articleRepository.existsByTitleOrOriginLink(title, originLink);
    }


    // 뉴스가 최근 n년 이내의 것인지 검증
    private boolean isRecentNews (LocalDateTime pubDate) {
        LocalDateTime yearsAgo = LocalDateTime.now().minusYears(3);
        return pubDate.isAfter(yearsAgo);
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
