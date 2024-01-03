package com.article.repository;

import com.article.entity.CompanyInfo;
import com.article.entity.TmpExport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyInfoRepository extends JpaRepository<CompanyInfo, Long> {

    /*@Query("SELECT d1.idSeq, d1.companyName, d1.ceoName " +
            "FROM CompanyInfo d1 " +
            "LEFT JOIN TmpExport d2 ON d2.idSeq = d1.idSeq " +
            "WHERE d1.terminationDate IS NULL " +
            "ORDER BY d2.idSeq")
    Page<CompanyInfo> findMatchingCompanies(Pageable pageable);*/

    @Query("SELECT i FROM CompanyInfo i WHERE idSeq IN (SELECT idSeq FROM TmpExport)")
    Page<CompanyInfo> findMatchingCompanies(Pageable pageable);

    @Query(nativeQuery = true, value =
            "SELECT t1.id_seq, t2.company_name, t2.ceo_name\n" +
                    "FROM kised.kised_target_231221 t1\n" +
                    "LEFT JOIN public.unv_company t2\n" +
                    "ON t1.id_seq = t2.id_seq\n" +
                    "WHERE t2.ceo_name IS NOT NULL\n" +
                    "ORDER BY t1.id_seq"
                    )
    List<Object[]> getCompanyInfos ();


}
