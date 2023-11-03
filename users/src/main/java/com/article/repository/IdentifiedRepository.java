package com.article.repository;

import com.article.entity.CompanyInfo;
import com.article.entity.Identified;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdentifiedRepository extends JpaRepository<Identified, Integer> {

    @Query("SELECT i FROM Identified i " +
            "LEFT JOIN CompanyInfo u ON i.idSeq = u.idSeq " +
            "WHERE u.terminationDate IS NULL " +
            "AND i.ceoName IS NOT NULL " +
            "AND COALESCE(u.corporateStatus, '살아있는 등기') IN ('회생절차', '살아있는 등기', '보전관리') " +
            "ORDER BY i.idSeq")
    Page<Identified> findMatchingCompanies(Pageable pageable);


}
