package com.article.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "woori_base", schema = "woori")
public class WooriBase {

    @Id
    @Column(name = "id_seq")
    private int idSeq;

    @Column(name = "enr")
    private String enr;

    @Column(name = "crn")
    private String crn;

    @Column(name = "is_identified")
    private boolean isIdentified;
}
