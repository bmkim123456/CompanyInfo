package com.article.article.service;

import com.article.article.component.SearchResultsProducer;
import com.article.article.dto.BigkindsResponse;
import com.article.article.dto.BigkindsRequestParam;
import com.article.article.dto.CompanySearchParam;
import com.article.article.entity.Article;
import com.article.article.repository.ArticleRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;


@Service
public class ArticleService {

    @Value("${bigkinds.api.url}")
    private String bigkindsUrl;

    @Value("{bigkinds.api.key")
    private String apiKey;

    private static final Logger log = LoggerFactory.getLogger(ArticleService.class);

    private final ArticleRepository articleRepository;
    private final RestTemplate restTemplate;
    private final SearchResultsProducer searchResultsProducer;

    public ArticleService(ArticleRepository articleRepository, RestTemplate restTemplate, SearchResultsProducer searchResultsProducer) {
        this.articleRepository = articleRepository;
        this.restTemplate = restTemplate;
        this.searchResultsProducer = searchResultsProducer;
    }

    AtomicLong KEY_COUNT = new AtomicLong(1);
    public String searchNaverArticle(CompanySearchParam searchParam) {
        try {
            if (searchParam.getCompanyName().isEmpty() || searchParam.getCeoName().isEmpty()) {
                log.info("회사명 또는 대표자명을 알 수 없습니다.");
            } else if (searchParam.getTermination().equals("CLOSED")) {
                log.info("수집을 진행하지 않습니다. 이유 : CLOSED");
            } else if (searchParam.getCorporateStatus().equals("살아있는 등기") || searchParam.getCorporateStatus().equals("회생절차")
                    || searchParam.getCorporateStatus().equals("보전관리")) {

                String encodedKeyword = encodeKeyword(searchParam.getCompanyName(), searchParam.getCeoName());
                log.info(searchParam.getCompanyName() + " " + searchParam.getCeoName());

                // 네이버 뉴스 검색 API 호출 URL 생성
                String apiUrl = "https://openapi.naver.com/v1/search/news.json?query=" + encodedKeyword + "&display=100";
                log.info("키 사용 횟수 : {}", KEY_COUNT.get());
                KEY_COUNT.incrementAndGet();

                HttpHeaders headers = createRequestHeaders();

                // API 호출
                ResponseEntity<String> responseEntity = restTemplate.exchange(
                        URI.create(apiUrl),
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        String.class
                );

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                JsonNode rootNode = objectMapper.readTree(responseEntity.getBody());
                JsonNode itemsNode = rootNode.get("items");

                if (itemsNode != null && itemsNode.isArray()) {

                    for (JsonNode itemNode : itemsNode) {

                        String title = itemNode.get("title").asText();
                        String originLink = itemNode.get("originallink").asText();
                        String pubDateStr = itemNode.get("pubDate").asText();

                        // pubDate 값을 LocalDateTime으로 변환
                        LocalDateTime pubDate = ZonedDateTime.parse(pubDateStr, DateTimeFormatter.RFC_1123_DATE_TIME).toLocalDateTime();

                        // 최근 n년 이내의 뉴스인지 확인
                        if (isRecentNews(pubDate)) {
                            // 뉴스 중복여부 확인 (제목, 원본링크)
                            if (!isDuplicateNews(title, originLink)) {
                                int idSeq = searchParam.getId_seq();
                                String link = itemNode.get("link").asText();
                                String description = itemNode.get("description").asText();
                                String source = "NAVER";
                                LocalDateTime createDatetime = LocalDateTime.now();
                                LocalDateTime updateDatetime = LocalDateTime.now();
                                Article news = createNaverEntity(createDatetime, updateDatetime, idSeq, source, originLink, link, pubDate, title, description);

                                // Naver entity에 수집된 정보를 rabbitmq로 전달
                                String searchResultJson = objectMapper.writeValueAsString(news);
                                searchResultsProducer.sendSearchResults(searchResultJson);
                            }
                        }
                    }
                } // log.info(responseEntity.getBody());
            } else log.info("사업 활동 중이 아닌 기업 입니다.");
            return "rabbitmq 전달 완료";
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패", e);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new RuntimeException("검색 실패", e);
            }
    }


    public BigkindsResponse searchBigkindsArticle (CompanySearchParam searchParam) {
        try {
            if (searchParam.getCompanyName().isEmpty() || searchParam.getCeoName().isEmpty()) {
                log.info("회사명 또는 대표자명을 알 수 없습니다.");
            } else if (searchParam.getTermination().equals("CLOSED")) {
                log.info("수집을 진행하지 않습니다. 이유 : CLOSED");
            } else if (searchParam.getCorporateStatus().equals("살아있는 등기") || searchParam.getCorporateStatus().equals("회생절차")
                    || searchParam.getCorporateStatus().equals("보전관리")) {

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                BigkindsRequestParam requestParam = new BigkindsRequestParam(searchParam.getCompanyName() + " " + searchParam.getCeoName());

                HttpEntity<BigkindsRequestParam> requestEntity = new HttpEntity<>(requestParam, headers);

                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<BigkindsResponse> response = restTemplate.exchange(bigkindsUrl, HttpMethod.POST, requestEntity, BigkindsResponse.class);
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                BigkindsResponse bigkindsResponse = response.getBody();

                if (bigkindsResponse != null && bigkindsResponse.getReturnObject() != null && bigkindsResponse.getReturnObject().getDocuments() != null) {
                    for (BigkindsResponse.Document document : bigkindsResponse.getReturnObject().getDocuments()) {
                        String title = document.getTitle();
                        String link = document.getProviderLinkPage();
                        String pubDateStr = document.getPublishedAt();

                        // pubDate 값을 LocalDateTime으로 변환
                        //LocalDateTime pubDate = ZonedDateTime.parse(pubDateStr, DateTimeFormatter.RFC_1123_DATE_TIME).toLocalDateTime();

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                        LocalDateTime pubDate = LocalDateTime.parse(pubDateStr, formatter);

                        if (!isDuplicateNews(title, link)) {
                            String newsId = document.getNewsId();
                            LocalDateTime createDatetime = LocalDateTime.now();
                            LocalDateTime updateDatetime = LocalDateTime.now();
                            int idSeq = searchParam.getId_seq();
                            String description = document.getContent();
                            String source = "BIGKINDS";
                            String publisher = document.getProvider();
                            String byline = document.getByline().replaceAll("\\|", " ");
                            String author = byline;


                            Article news = createBigkindsEntity(newsId, createDatetime, updateDatetime, idSeq, source, link, pubDate, publisher, title, description, author);

                            String searchResultJson = objectMapper.writeValueAsString(news);
                            searchResultsProducer.sendSearchResults(searchResultJson);
                        }
                    }
                }
            } else log.info("사업 활동 중이 아닌 기업 입니다.");
            return null;
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new RuntimeException("검색 실패", e);
        }
    }


    // 회사명 + 대표자명 키워드 전달
    private String encodeKeyword(String companyName, String ceoName) throws UnsupportedEncodingException {
        String keyword = companyName + " " + ceoName;
        return URLEncoder.encode(keyword, "UTF-8");
    }

    // 뉴스 rabbitmq로 전달
    private Article createNaverEntity(LocalDateTime createDataTime, LocalDateTime updateDatetime, int idSeq, String source, String originalLink, String link,
                                      LocalDateTime pubDate, String title, String description) {
        Article news = new Article();
        news.setCreateDatetime(createDataTime);
        news.setUpdateDatetime(updateDatetime);
        news.setIdSeq(idSeq);
        news.setSource(source);
        news.setOriginLink(originalLink);
        news.setLink(link);
        news.setPublishDatetime(pubDate);
        news.setTitle(title);
        news.setPrevContent(description);
        return news;
    }

    private Article createBigkindsEntity(String newsId, LocalDateTime createDataTime, LocalDateTime updateDatetime, int idSeq, String source, String link,
                                         LocalDateTime pubDate, String publisher, String title, String description, String author) {
        Article news = new Article();
        news.setNewsId(newsId);
        news.setCreateDatetime(createDataTime);
        news.setUpdateDatetime(updateDatetime);
        news.setIdSeq(idSeq);
        news.setSource(source);
        news.setLink(link);
        news.setPublishDatetime(pubDate);
        news.setPublisher(publisher);
        news.setTitle(title);
        news.setPrevContent(description);
        news.setAuthor(author);
        return news;
    }


    // 중복 뉴스 여부를 검사
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
        }
        return headers;
    }
}
