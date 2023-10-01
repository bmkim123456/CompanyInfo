package com.article.article.service;

import com.article.article.dto.CompanySearchParam;
import com.article.article.entity.Naver;
import com.article.article.mapper.JsonMapper;
import com.article.article.repository.NaverRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Locale;

@Service
public class NaverArticleService {

    /*@Value("${naver.api.clientId}")
    private String clientId;

    @Value("${naver.api.clientSecret}")
    private String clientSecret;*/

    private final NaverRepository naverRepository;
    private final RestTemplate restTemplate;
    private final JsonMapper jsonMapper;

    public NaverArticleService(NaverRepository naverRepository, RestTemplate restTemplate, JsonMapper jsonMapper) {
        this.naverRepository = naverRepository;
        this.restTemplate = restTemplate;
        this.jsonMapper = jsonMapper;
    }

    String clientId = "N97AIPxC798NeqYQQBDv";
    String clientSecret = "cXcIIQ78M8";

    public String searchArticle(CompanySearchParam searchParam) {
        try {
            // 검색에 사용할 키워드 조합
            String keyword = searchParam.getCompanyName() + " " + searchParam.getCeoName();
            String encodedKeyword = URLEncoder.encode(keyword, "UTF-8");

            // 네이버 블로그 검색 API 호출 URL 생성
            String apiUrl = "https://openapi.naver.com/v1/search/news?query=" + encodedKeyword;

            // 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.set("X-Naver-Client-Id", clientId);
            headers.set("X-Naver-Client-Secret", clientSecret);

            // API 호출
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    URI.create(apiUrl),
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    String.class
            );

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = jsonMapper.readTree(responseEntity.getBody());
            JsonNode itemsNode = rootNode.get("items");

            if (itemsNode != null && itemsNode.isArray()) {
                for (JsonNode itemNode : itemsNode) {
                    String title = itemNode.get("title").asText();
                    String originalLink = itemNode.get("originallink").asText();
                    String link = itemNode.get("link").asText();
                    String description = itemNode.get("description").asText();
                    String pubDateStr = itemNode.get("pubDate").asText();

                    // pubDate 값을 LocalDateTime으로 변환
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
                    LocalDateTime pubDate = LocalDateTime.parse(pubDateStr, formatter);

                    // 엔터티 객체 생성 및 데이터 설정
                    Naver news = new Naver();
                    news.setTitle(title);
                    news.setOrigin_link(originalLink);
                    news.setLink(link);
                    news.setPrev_content(description);
                    news.setUpdate_datetime(pubDate);

                    // 엔터티를 DB에 저장
                    naverRepository.save(news);
                }
            }

            return "검색 완료";
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패", e);
        }
    }
}
