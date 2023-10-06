package com.article.article.repository;

import com.article.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    // 뉴스 중복 체크
    boolean existsByTitleOrOriginLink(String title, String originLink);
}
