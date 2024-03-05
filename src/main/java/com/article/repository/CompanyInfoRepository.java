package com.article.repository;

import com.article.entity.CompanyInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public interface CompanyInfoRepository extends JpaRepository<CompanyInfo, Long> {

    @Query(value =
           "SELECT t1 " +
           "FROM CompanyInfo t1 " +
           "JOIN FETCH t1.company t2 " +
           "WHERE t1.idSeq = t2.idSeq " +
           "ORDER BY t2.idSeq ASC ")
    List<CompanyInfo> findCompany();




}
