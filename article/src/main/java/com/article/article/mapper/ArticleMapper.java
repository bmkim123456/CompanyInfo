package com.article.article.mapper;

import com.article.article.dto.BigkindsResponse;
import com.article.article.entity.Article;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper
public interface ArticleMapper {

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
}
