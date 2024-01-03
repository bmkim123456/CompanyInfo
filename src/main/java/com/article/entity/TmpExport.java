package com.article.entity;

import jdk.jfr.ContentType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "kt_report_231129", schema = "kt")
public class TmpExport {

    @Id
    @Column(name = "id_seq")
    private int idSeq;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "ceo_name")
    private String ceoName;

}
