package com.article.controller;


import com.article.service.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

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


    // 뉴스 함께 검색
    /*@PostMapping("/search")
    public ResponseEntity<List<NaverNewsItem>> processUsers(@RequestParam(name = "display", required = false, defaultValue = "10") int display) {
        List<NaverNewsItem> newsItems = testService.processUsersSequentially(display);

        if (!newsItems.isEmpty()) {
            return ResponseEntity.ok(newsItems);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }*/



}
