package com.article.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "unv_company", schema = "public")
public class CompanyInfo {

    @Id
    @Column(name = "id_seq")
    private Long idSeq;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "ceo_name")
    private String ceoName;

    @Column(name = "corporate_status")
    private String corporateStatus;

    @Column(name = "termination_date")
    private LocalDate terminationDate;
}
