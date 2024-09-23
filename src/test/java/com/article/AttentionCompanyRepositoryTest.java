package com.article;

import com.article.dto.CompanyDto;
import com.article.entity.AttentionCompany;
import com.article.entity.CompanyInfo;
import com.article.repository.AttentionCompanyRepository;
import com.article.repository.CompanyInfoRepository;
import com.article.service.CompanySearchService;
import org.json.JSONObject;
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

    @Test
    void getCompanyInfoTest () {

        CompanyDto dto = new CompanyDto();
        dto.setIdSeq(336484L);
        dto.setCompanyName("앤톡");
        dto.setCeoName("박재준");
        dto.setKeyword("투자");
        dto.setKeyword2("유치");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("idSeq", dto.getIdSeq());
        jsonObject.put("companyName", dto.getCompanyName());
        if (dto.getCeoName() == null) {
            jsonObject.put("ceoName", "");
        } else jsonObject.put("ceoName", dto.getCeoName());
        jsonObject.put("keyword", dto.getKeyword());
        jsonObject.put("keyword2", dto.getKeyword2());

        System.out.println(jsonObject);
    }
}
