package com.article.article.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "exception", schema = "public")
public class LogRecord {

    @Id
    private int idSeq;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "reason")
    private String reason;
}
