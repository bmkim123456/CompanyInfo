package com.article.article.mapper;

import com.article.article.ArticleApplication;
import com.article.article.dto.BigkindsResponse;
import com.article.article.dto.CompanySearchParam;
import com.article.article.dto.NaverResponse;
import com.article.article.entity.Article;
import com.article.article.entity.ArticleCnt;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Mapper
public interface ArticleMapper {

    // 네이버 기사 검색 결과를 Article entity 컬럼과 일치하도록 매핑
    default Article naverResponseToArticle (NaverResponse.Items items, CompanySearchParam searchParam) {

        Article naverArticles = new Article();
        naverArticles.setIdSeq(searchParam.getId_seq());
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
    default Article bigkindsResponseToArticle (BigkindsResponse.Document response, CompanySearchParam searchParam) {

        Article bigkindsArticle = new Article();
        bigkindsArticle.setCreateDatetime(LocalDateTime.now());
        bigkindsArticle.setUpdateDatetime(LocalDateTime.now());
        bigkindsArticle.setIdSeq(searchParam.getId_seq());
        bigkindsArticle.setSource("BIGKINDS");
        bigkindsArticle.setNewsId(response.getNewsId());
        bigkindsArticle.setOriginLink(response.getProviderLinkPage());
        bigkindsArticle.setPublishDatetime(response.getPublishedAt());
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

    default ArticleCnt searchArticleCnt (Article article, CompanySearchParam searchParam) {

        ArticleCnt articleCnt = new ArticleCnt();

         int y = article.getPublishDatetime().getYear();
         int m = article.getPublishDatetime().getMonthValue();
         int d = article.getPublishDatetime().getDayOfMonth();

         articleCnt.setIdSeq(searchParam.getId_seq());
         articleCnt.setArticleY(y);
         articleCnt.setArticleM(m);
         articleCnt.setArticleD(d);
         articleCnt.setArticleCnt(1);
         articleCnt.setArticleYMD(article.getPublishDatetime().toLocalDate());

        return articleCnt;
    }


    default ArticleCnt articleCntResult (ArticleCnt articleCnt) {

        ArticleCnt articleResult = new ArticleCnt();
        articleResult.setIdSeq(articleCnt.getIdSeq());
        articleResult.setArticleY(articleCnt.getArticleY());
        articleResult.setArticleM(articleCnt.getArticleM());
        articleResult.setArticleD(articleCnt.getArticleD());
        articleResult.setArticleYMD(articleCnt.getArticleYMD());
        articleResult.setArticleCnt(articleCnt.getArticleCnt());
        return articleResult;

    }

}
