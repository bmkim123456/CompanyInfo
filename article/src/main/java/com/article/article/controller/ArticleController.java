package com.article.article.controller;

import com.article.article.dto.BigkindsResponse;
import com.article.article.dto.CompanySearchParam;
import com.article.article.service.BigkindsArticleService;
import com.article.article.service.NaverArticleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/article")
public class ArticleController {


    private final NaverArticleService naverArticleService;
    private final BigkindsArticleService bigkindsArticleService;

    public ArticleController(NaverArticleService naverArticleService, BigkindsArticleService bigkindsArticleService) {
        this.naverArticleService = naverArticleService;
        this.bigkindsArticleService = bigkindsArticleService;
    }

    @PostMapping("/naver")
    public ResponseEntity<String> searchArticle(@RequestBody CompanySearchParam searchParam) {
        String result = naverArticleService.searchNaverArticle(searchParam);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/bigkinds")
    public ResponseEntity<BigkindsResponse> searchArticleBigkinds(@RequestBody CompanySearchParam searchParam) throws JsonProcessingException {
        BigkindsResponse result = bigkindsArticleService.searchBigkindsArticle(searchParam);
        return ResponseEntity.ok(result);
    }
}
