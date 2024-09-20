package com.article;

import com.article.entity.AttentionCompany;
import com.article.entity.CompanyInfo;
import com.article.repository.AttentionCompanyRepository;
import com.article.repository.CompanyInfoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AttentionCompanyRepositoryTest {

    @Autowired
    private CompanyInfoRepository companyInfoRepository;


    @Test
    void getPartCompany () {

        List<CompanyInfo> companyInfoList = companyInfoRepository.getCompanyInfo()
                .stream()
                .limit(10)
                .collect(Collectors.toList());
        assertThat(companyInfoList).isNotNull();

        for (CompanyInfo companyInfo : companyInfoList) {
            System.out.println(companyInfo.getCompanyName());
        }
    }
}
