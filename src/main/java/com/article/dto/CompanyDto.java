package com.article.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CompanyDto {

    private Long idSeq;
    private String enr;
    private String companyName;
    private String ceoName;
    private String keyword;
    private String keyword2;
    private String queue;
    private MultipartFile file;
    private String requestDate;
}
