package com.article.article.repository;

import com.article.article.entity.ArticleCnt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ArticleCntRepository extends JpaRepository<ArticleCnt, Integer> {

    boolean existsByIdSeqAndArticleYMD (Integer idSeq, LocalDate articleYMD);

    ArticleCnt findByIdSeqAndArticleYMD (Integer idSeq, LocalDate articleYMD);
}
