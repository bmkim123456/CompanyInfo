package com.article.article.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Id;
import java.time.LocalDateTime;

// 전달받을 기업 정보
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanySearchParam {

    @Id
    private int id_seq;

    private String companyName;

    private String ceoName;

    private String corporateStatus;

    private String termination;

    private LocalDateTime articleUpdateDatetime;
}
