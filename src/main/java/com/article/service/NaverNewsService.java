package com.article.service;

import com.article.entity.NaverNewsItem;
import com.article.etc.NaverNewsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class NaverNewsService {

    @Value("${naver.api.clientId}")
    private String clientId;

    @Value("${naver.api.clientSecret}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    public NaverNewsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<NaverNewsItem> searchNaverNews(String keyword, int display) {
        String apiUrl = "https://openapi.naver.com/v1/search/news.json?query=" + keyword + "&display=" + display;

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        NaverNewsResponse response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                NaverNewsResponse.class
        ).getBody();

        if (response != null && response.getItems() != null) {
            return response.getItems();
        }

        return Collections.emptyList();
    }
}
