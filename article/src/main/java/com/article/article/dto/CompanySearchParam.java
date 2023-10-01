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

    private String companyName;

    private String ceoName;

    private LocalDateTime articleUpdateDatetime;
}
