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
    private AttentionCompanyRepository attentionCompanyRepository;

    @Autowired
    private CompanyInfoRepository companyInfoRepository;

    @Test
    public void testGetCompany() {
        try (Stream<AttentionCompany> companies = attentionCompanyRepository.getCompanyInfo()) {
            assertThat(companies).isNotNull();

            List<AttentionCompany> companyList = companies.limit(10).collect(Collectors.toList());
            assertThat(companyList).isNotNull();

            for (AttentionCompany companyInfo : companyList) {
                System.out.println(companyInfo.getCompanyInfo().getCompanyName());
            }
        }
    }

    @Test
    public void getEnrTest() {
        try (Stream<String> companyEnr = companyInfoRepository.getCompanyEnr()) {
            assertThat(companyEnr).isNotNull();

            List<String> enrList = companyEnr.limit(10).collect(Collectors.toList());
            assertThat(enrList).isNotNull();

            for (String enr : enrList) {
                System.out.println(enr);
            }
        }
    }
}
