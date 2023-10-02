package com.article.article.service;

import com.article.article.dto.CompanySearchParam;
import com.article.article.entity.Naver;
import com.article.article.repository.NaverRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Locale;


@Service
public class NaverArticleService {

    @Value("${naver.api.clientId}")
    private String clientId;

    @Value("${naver.api.clientSecret}")
    private String clientSecret;

    private final NaverRepository naverRepository;
    private final RestTemplate restTemplate;

    public NaverArticleService(NaverRepository naverRepository, RestTemplate restTemplate) {
        this.naverRepository = naverRepository;
        this.restTemplate = restTemplate;
    }

    public String searchArticle(CompanySearchParam searchParam) {
        try {
            // 검색에 사용할 키워드 조합
            if (searchParam.getCompanyName().isEmpty() || searchParam.getCeoName().isEmpty()) {
                throw new RuntimeException("회사명 또는 대표자명을 알 수 없습니다.");
            } else {

                String encodedKeyword = encodeKeyword(searchParam.getCompanyName(), searchParam.getCeoName());

                // 네이버 뉴스 검색 API 호출 URL 생성
                String apiUrl = "https://openapi.naver.com/v1/search/news?query=" + encodedKeyword;

                HttpHeaders headers = createRequestHeaders();

                // API 호출
                ResponseEntity<String> responseEntity = restTemplate.exchange(
                        URI.create(apiUrl),
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        String.class
                );

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(responseEntity.getBody());
                JsonNode itemsNode = rootNode.get("items");

                if (itemsNode != null && itemsNode.isArray()) {
                    int maxNewsToSave = 100;

                    for (JsonNode itemNode : itemsNode) {
                        if (maxNewsToSave <= 0) {
                            break;
                        }

                        String title = itemNode.get("title").asText();
                        String originalLink = itemNode.get("originallink").asText();
                        String link = itemNode.get("link").asText();
                        String description = itemNode.get("description").asText();
                        String pubDateStr = itemNode.get("pubDate").asText();

                        // pubDate 값을 LocalDateTime으로 변환
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
                        LocalDateTime pubDate = LocalDateTime.parse(pubDateStr, formatter);

                        // 엔터티 객체 생성 및 데이터 설정
                        Naver news = createNaverEntity(searchParam.getId_seq(), title, originalLink, link, description, pubDate);

                        // 엔터티를 DB에 저장
                        naverRepository.save(news);

                        maxNewsToSave--;
                    }
                }
            }
            return "저장완료";
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패", e);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new RuntimeException("검색 실패", e);
            }
    }

    // 회사명 + 대표자명 키워드 전달
    private String encodeKeyword(String companyName, String ceoName) throws UnsupportedEncodingException {
        String keyword = companyName + " " + ceoName;
        return URLEncoder.encode(keyword, "UTF-8");
    }

    // 요청 헤더 설정
    private HttpHeaders createRequestHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);
        return headers;
    }

    // 뉴스 저장
    private Naver createNaverEntity(int id_seq, String title, String originalLink, String link, String description, LocalDateTime pubDate) {
        Naver news = new Naver();
        news.setId_seq(id_seq);
        news.setTitle(title);
        news.setOrigin_link(originalLink);
        news.setLink(link);
        news.setPrev_content(description);
        news.setUpdate_datetime(pubDate);
        return news;
    }
}
