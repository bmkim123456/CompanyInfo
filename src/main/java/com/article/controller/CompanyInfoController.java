package com.article.controller;


import com.article.service.CompanySearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CompanyInfoController {

    private final CompanySearchService searchService;


    @PostMapping("/searchUrl")
    public ResponseEntity<String> companyInfoUrl() {

        String result = searchService.sendCompanyInfoUrl();

        return ResponseEntity.ok(result);
    }

    @PostMapping("/searchQueue")
    public ResponseEntity<String> companyInfoQueue() {

        String result = searchService.sendToCompanyInfoQueue();

        return ResponseEntity.ok(result);
    }

    @PostMapping("/search/article_cnt")
    public ResponseEntity<String> companyInfoQueueCnt() {
        String result = searchService.sendToCompanyInfoQueueCnt();
        return ResponseEntity.ok(result);
    }

}
