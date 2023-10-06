package com.article.repository;

import com.article.entity.CompanyInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyInfoRepository extends JpaRepository<CompanyInfo, Long> {

    List<CompanyInfo> findByIdSeq(Long idSeq);

}
