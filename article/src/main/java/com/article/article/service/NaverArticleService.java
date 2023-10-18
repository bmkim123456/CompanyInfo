package com.article.article.service;

import com.article.article.dto.CompanySearchParam;
import com.article.article.dto.NaverResponse;
import com.article.article.entity.Article;
import com.article.article.entity.ArticleCnt;
import com.article.article.mapper.ArticleMapper;
import com.article.article.repository.ArticleCntRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class NaverArticleService {

    private static final Logger log = LoggerFactory.getLogger(NaverArticleService.class);

    private final ArticleCntRepository articleCntRepository;
    private final ArticleMapper articleMapper;
    private final RestTemplate restTemplate;

    public NaverArticleService(ArticleCntRepository articleCntRepository, ArticleMapper articleMapper, RestTemplate restTemplate) {
        this.articleCntRepository = articleCntRepository;
        this.articleMapper = articleMapper;
        this.restTemplate = restTemplate;
    }

    // 네이버 api키 사용횟수 카운트 초기화
    AtomicLong KEY_COUNT = new AtomicLong(1);

    // 네이버 뉴스기사 검새 로직
    public List<Article> searchNaverArticles (CompanySearchParam searchParam) throws InterruptedException, JsonProcessingException {

        int start = 1;
        List<Article> naverArticle = new ArrayList<>();

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
            NaverResponse naverResponse = response.getBody();

            int total = naverResponse.getTotal();
            int responseSize = 0;

            if (naverResponse != null && naverResponse.getItems() != null) {

                List<NaverResponse.Items> lastDateItems = Arrays.stream(naverResponse.getItems())
                        .filter(items -> {
                            if (searchParam.getId_seq() != 0) {
                                ArticleCnt latestArticleCnt = articleCntRepository.findFirstByidSeqOrderByArticleYMDDesc(searchParam.getId_seq());
                                if (latestArticleCnt != null) {
                                    LocalDate latestDate = latestArticleCnt.getArticleYMD();
                                    return items.getPubDate().toLocalDate().isAfter(latestDate);
                                }
                            }
                            return true;
                        })
                        .collect(Collectors.toList());

                responseSize = lastDateItems.size();

                for (NaverResponse.Items items : lastDateItems) {

                    // 수집한 기사를 리스트에 추가
                    Article sendNaverArticle = articleMapper.naverResponseToArticle(items, searchParam);
                    naverArticle.add(sendNaverArticle);

                }
            }

            if (start > 1000 || start > total || responseSize < 100) {
                log.info("네이버 기사 총 {}건 검색 되었습니다.", total);
                break;
            }

            Thread.sleep(500);

        }
        return naverArticle;
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
