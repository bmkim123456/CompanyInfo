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

    @Query(nativeQuery = true, value = "SELECT id_seq, company_name, ceo_name\n" +
            "FROM unv_company\n" +
            "ORDER BY id_seq\n" +
            "LIMIT 1")
    List<CompanyInfo> findCompany();




}
