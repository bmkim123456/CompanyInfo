package com.article.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Builder
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

    @ManyToOne
    @JoinColumn(name = "id_seq",insertable = false, updatable = false)
    private ArticleCompany company;

}
