package com.article.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "attention_company", schema = "mgmt")
public class AttentionCompany {

    @Column(name = "organization_seq")
    private Long organizationSeq;

    @Id
    @Column(name = "id_seq")
    private Long idSeq;

    @ManyToOne
    @JoinColumn(name = "id_seq", insertable = false, updatable = false)
    private CompanyInfo companyInfo;

}
