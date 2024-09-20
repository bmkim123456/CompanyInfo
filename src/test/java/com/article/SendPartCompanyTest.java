package com.article;

import com.article.service.CompanySearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
public class SendPartCompanyTest {

    @Autowired
    CompanySearchService service;

    @Test
    void getCompany () {


    }
}
