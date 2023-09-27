package com.article.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "ceo_name_asis", schema = "asis")
public class CompanyInfo {

    @Id
    private Long id_seq;

    @Column(name = "\"companyName\"")
    private String companyName;

    @Column(name = "\"ceoName\"")
    private String ceoName;
}
