package com.article.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "unv_company", schema = "public")
public class CompanyInfo {

    @Id
    @Column(name = "id_seq")
    private Long idSeq;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "ceo_name")
    private String ceoName;

    @Column(name = "enr")
    private String enr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_seq", insertable = false, updatable = false)
    private AttentionCompany attentionCompany;

}
