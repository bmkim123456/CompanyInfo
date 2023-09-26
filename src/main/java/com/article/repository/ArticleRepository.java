package com.article.repository;

import com.article.entity.NaverNewsItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<NaverNewsItem, Long> {
}
