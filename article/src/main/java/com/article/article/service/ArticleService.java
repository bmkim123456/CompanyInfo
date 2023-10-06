package com.article.article.service;

import com.article.article.dto.BigkindsResponse;
import com.article.article.dto.CompanySearchParam;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {

    private final NaverArticleService naverArticleService;
    private final BigkindsArticleService bigkindsArticleService;

    public ArticleService(NaverArticleService naverArticleService, BigkindsArticleService bigkindsArticleService) {
        this.naverArticleService = naverArticleService;
        this.bigkindsArticleService = bigkindsArticleService;
    }

    public void articleSearch (CompanySearchParam searchParam) {

        String naver = naverArticleService.searchNaverArticle(searchParam);
        BigkindsResponse bigkinds = bigkindsArticleService.searchBigkindsArticle(searchParam);
    }
}
