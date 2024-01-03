package com.article.repository;

import com.article.entity.Identified;
import com.article.entity.TmpExport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TmpExportRepository extends JpaRepository<TmpExport, Integer> {

    @Query("SELECT d1 " +
            "FROM TmpExport d1 " +
            // "LEFT JOIN CompanyInfo d2 ON d2.idSeq = d1.idSeq " +
            // "WHERE d2.terminationDate IS NULL " +
            "ORDER BY d1.idSeq")
    Page<TmpExport> findMatchingCompanies(Pageable pageable);
}
