package com.article.repository;

import com.article.entity.CompanyInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public interface CompanyInfoRepository extends JpaRepository<CompanyInfo, Long> {

    @Query("SELECT t1 FROM CompanyInfo t1 WHERE t1.idSeq = :idSeq")
    CompanyInfo getCompanyInfo (@Param("idSeq") Long id);

}
