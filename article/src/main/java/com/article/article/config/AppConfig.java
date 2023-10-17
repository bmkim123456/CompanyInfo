package com.article.article.config;

import com.article.article.dto.BigkindsResponse;
import com.article.article.dto.CompanySearchParam;
import com.article.article.dto.NaverResponse;
import com.article.article.entity.Article;
import com.article.article.entity.ArticleCnt;
import com.article.article.entity.LogRecord;
import com.article.article.mapper.ArticleMapper;
import com.article.article.mapper.LogMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // 매핑자료 bean 등록
    @Bean
    public ArticleMapper articleMapper() {
        return new ArticleMapper() {
            @Override
            public Article naverResponseToArticle(NaverResponse.Items items, CompanySearchParam searchParam) {
                return ArticleMapper.super.naverResponseToArticle(items, searchParam);
            }

            @Override
            public Article bigKindsResponseToArticle (BigkindsResponse.Document response, CompanySearchParam searchParam) {
                return ArticleMapper.super.bigKindsResponseToArticle(response, searchParam);
            }

            @Override
            public Article createArticle(Article article) {
                return ArticleMapper.super.createArticle(article);
            }

            @Override
            public ArticleCnt searchArticleCnt (Article article, CompanySearchParam searchParam) {
                return ArticleMapper.super.searchArticleCnt(article, searchParam);
            }

            @Override
            public ArticleCnt articleCntResult (ArticleCnt articleCnt) {
                return ArticleMapper.super.articleCntResult(articleCnt);
            }
        };
    }

    @Bean
    public LogMapper logMapper() {
        return new LogMapper() {
            @Override
            public LogRecord companySearchParamToLogRecord(CompanySearchParam companySearchParam) {
                return LogMapper.super.companySearchParamToLogRecord(companySearchParam);
            }
        };
    }
}
