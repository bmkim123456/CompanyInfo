package com.article.controller;

import com.article.service.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ArticleController {

    private final ArticleService testService;

    public ArticleController(ArticleService testService) {
        this.testService = testService;
    }

    // 회사 정보 조회
    @PostMapping("/search")
    public ResponseEntity<String> processUsers() {

        String result = testService.processUsersSequentially();

        return ResponseEntity.ok(result);
    }
}
