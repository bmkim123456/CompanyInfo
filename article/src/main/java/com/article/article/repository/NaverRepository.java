package com.article.article.repository;

import com.article.article.entity.Naver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NaverRepository extends JpaRepository<Naver, Long> {

    // 뉴스 중복 체크
    boolean existsByTitleOrOriginLink(String title, String originLink);
}
