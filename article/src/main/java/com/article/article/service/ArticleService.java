package com.article.article.service;

import com.article.article.dto.BigkindsResponse;
import com.article.article.dto.CompanySearchParam;
import com.article.article.dto.NaverResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {

    private final NaverArticleService naverArticleService;
    private final BigkindsArticleService bigkindsArticleService;

    public ArticleService(NaverArticleService naverArticleService, BigkindsArticleService bigkindsArticleService) {
        this.naverArticleService = naverArticleService;
        this.bigkindsArticleService = bigkindsArticleService;
    }

    public void articleSearch (CompanySearchParam searchParam) throws JsonProcessingException {

        NaverResponse naver = naverArticleService.searchNaverArticles(searchParam);
        BigkindsResponse bigkinds = bigkindsArticleService.searchBigkindsArticle(searchParam);

    }
}
