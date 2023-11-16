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

    @Query("SELECT i FROM TmpExport i " +
            "ORDER BY i.idSeq")
    Page<TmpExport> findMatchingCompanies(Pageable pageable);
}
