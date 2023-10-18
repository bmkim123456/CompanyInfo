package com.article.article.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


// 대표자명 null일 때 해당 기업 저장
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "log_record", schema = "public")
public class LogRecord {

    @Id
    private int idSeq;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "reason")
    private String reason;
}
