package com.article.article.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanySearchParam {

    @Id
    private int id_seq;

    @Column(name = "\"companyName\"")
    private String companyName;

    @Column(name = "\"ceoName\"")
    private String ceoName;

    private LocalDateTime articleUpdateDatetime;
}
