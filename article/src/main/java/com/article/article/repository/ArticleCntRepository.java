package com.article.article.repository;

import com.article.article.entity.ArticleCnt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ArticleCntRepository extends JpaRepository<ArticleCnt, Integer> {
    LocalDateTime existsByArticleYMD(LocalDateTime articleYMD);
    ArticleCnt findByArticleYMD(LocalDateTime articleYMD);
}
