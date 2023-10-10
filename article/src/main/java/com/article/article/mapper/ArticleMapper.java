package com.article.article.mapper;

import com.article.article.ArticleApplication;
import com.article.article.dto.BigkindsResponse;
import com.article.article.dto.NaverResponse;
import com.article.article.entity.Article;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper
public interface ArticleMapper {

    // 네이버 기사 검색 결과를 Article entity 컬럼과 일치하도록 매핑
    default Article naverResponseToArticle (NaverResponse.Items items) {

        Article naverArticles = new Article();

        naverArticles.setCreateDatetime(LocalDateTime.now());
        naverArticles.setUpdateDatetime(LocalDateTime.now());
        naverArticles.setSource("NAVER");
        naverArticles.setOriginLink(items.getOriginalLink());
        naverArticles.setLink(items.getLink());
        naverArticles.setTitle(items.getTitle());
        naverArticles.setPrevContent(items.getDescription());
        naverArticles.setPublishDatetime(items.getPubDate());

        return naverArticles;
    }

    // 빅카인즈 기사 검색 결과를 Article entity 컬럼과 일치하도록 매핑
    default Article bigkindsResponseToArticle (BigkindsResponse.Document response) {

        Article bigkindsArticle = new Article();

        // 뉴스 발행일 String 타입으로 수집 후 LocalDateTime 형태로 변환
        String pubDateStr = response.getPublishedAt();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        LocalDateTime pubDate = LocalDateTime.parse(pubDateStr, formatter);

        bigkindsArticle.setCreateDatetime(LocalDateTime.now());
        bigkindsArticle.setUpdateDatetime(LocalDateTime.now());
        bigkindsArticle.setSource("BIGKINDS");
        bigkindsArticle.setNewsId(response.getNewsId());
        bigkindsArticle.setLink(response.getProviderLinkPage());
        bigkindsArticle.setPublishDatetime(pubDate);
        bigkindsArticle.setPublisher(response.getProvider());
        bigkindsArticle.setTitle(response.getTitle());
        bigkindsArticle.setPrevContent(response.getContent());

        // 기자명 수집 시 | 특수기호 제외
        String byline = response.getByline().replaceAll("\\|", " ");
        bigkindsArticle.setAuthor(byline);

        return bigkindsArticle;
    }

    // 큐에 저장된 기사 정보를 Article entity 컬럼과 일치하도록 매핑
    default Article createArticle (Article article) {
        Article news = new Article();
        news.setIdSeq(article.getIdSeq());
        news.setSource(article.getSource());
        news.setCreateDatetime(article.getCreateDatetime());
        news.setTitle(article.getTitle());
        news.setOriginLink(article.getOriginLink());
        news.setLink(article.getLink());
        news.setPrevContent(article.getPrevContent());
        news.setUpdateDatetime(article.getUpdateDatetime());
        news.setPublishDatetime(article.getPublishDatetime());
        news.setNewsId(article.getNewsId());
        news.setPublisher(article.getPublisher());
        news.setAuthor(article.getAuthor());
        return news;
    }

}
