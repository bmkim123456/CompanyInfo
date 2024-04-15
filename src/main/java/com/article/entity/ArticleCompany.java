package com.article.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(schema = "ibk_article", name = "ibk240403")
public class ArticleCompany {

    @Id
    @Column(name = "id_seq")
    private Integer idSeq;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<CompanyInfo> companyInfo;
}
