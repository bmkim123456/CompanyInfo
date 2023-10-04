package com.article.article.controller;

import com.article.article.dto.ApiResponse;
import com.article.article.dto.CompanySearchParam;
import com.article.article.service.NaverArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/article")
public class ArticleController {

    private final NaverArticleService naverArticleService;

    public ArticleController(NaverArticleService naverArticleService) {
        this.naverArticleService = naverArticleService;
    }

    @PostMapping("/search")
    public ResponseEntity<String> searchArticle(@RequestBody CompanySearchParam searchParam) {
        String result = naverArticleService.searchArticle(searchParam);
        return ResponseEntity.ok(result);
    }
}
