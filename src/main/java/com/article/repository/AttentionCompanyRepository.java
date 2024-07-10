package com.article.repository;

import com.article.entity.AttentionCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface AttentionCompanyRepository extends JpaRepository<AttentionCompany, Long> {

    @Query("SELECT t1 FROM AttentionCompany t1 INNER JOIN t1.companyInfo")
    Stream<AttentionCompany> getCompanyInfo();
}
