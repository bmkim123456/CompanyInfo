package com.article.article.controller;

import com.article.article.dto.BigkindsRequestParam;
import com.article.article.dto.BigkindsResponse;
import com.article.article.dto.CompanySearchParam;
import com.article.article.service.ArticleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/article")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("/search")
    public ResponseEntity<String> searchArticle(@RequestBody CompanySearchParam searchParam) {
        String result = articleService.searchNaverArticle(searchParam);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/bigkinds")
    public ResponseEntity<BigkindsResponse> searchArticleBigkinds(@RequestBody CompanySearchParam searchParam) throws JsonProcessingException {
        BigkindsResponse result = articleService.searchBigkindsArticle(searchParam);
        return ResponseEntity.ok(result);
    }
}
