package com.article.article.controller;

import com.article.article.dto.ApiResponse;
import com.article.article.dto.CompanySearchParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/article")
public class ArticleController {

    @PostMapping("/search")
    public ResponseEntity<ApiResponse> search (@RequestBody CompanySearchParam cp) {
        return null;
    }
}
