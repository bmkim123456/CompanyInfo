package com.article.article.controller;

import com.article.article.dto.CompanySearchParam;
import com.article.article.entity.Article;
import com.article.article.service.ArticleService;
import com.article.article.service.BigkindsArticleService;
import com.article.article.service.NaverArticleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/article")
public class ArticleController {


    private final NaverArticleService naverArticleService;
    private final BigkindsArticleService bigkindsArticleService;
    private final ArticleService articleService;

    public ArticleController(NaverArticleService naverArticleService, BigkindsArticleService bigkindsArticleService, ArticleService articleService) {
        this.naverArticleService = naverArticleService;
        this.bigkindsArticleService = bigkindsArticleService;
        this.articleService = articleService;
    }

    @PostMapping("/naver")
    public ResponseEntity<List<Article>> searchArticle (@RequestBody CompanySearchParam searchParam)
                                                throws InterruptedException, JsonProcessingException {
        List<Article> result = naverArticleService.searchNaverArticles(searchParam);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/bigkinds")
    public ResponseEntity<List<Article>> searchArticleBigkinds(@RequestBody CompanySearchParam searchParam) throws JsonProcessingException {
        List<Article> result = bigkindsArticleService.searchBigkindsArticle(searchParam);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/article")
    public ResponseEntity<String> searchArticles (@RequestBody CompanySearchParam searchParam) throws JsonProcessingException {

        articleService.articleSearch(searchParam);
        return ResponseEntity.ok("검색 완료");
    }
}
