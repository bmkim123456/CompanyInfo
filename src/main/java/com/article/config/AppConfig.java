package com.article.config;

import com.article.entity.CompanyInfo;
import com.article.entity.Identified;
import com.article.entity.TmpExport;
import com.article.mapper.CompanyInfoMapper;
import com.article.util.EncryptionUtil;
import org.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public EncryptionUtil encryptionUtil() {
        return new EncryptionUtil();
    }

    @Bean
    public CompanyInfoMapper companyInfoMapper() {
        return new CompanyInfoMapper() {
            @Override
            public JSONObject companyInfoToJson(CompanyInfo companyInfo) {
                return CompanyInfoMapper.super.companyInfoToJson(companyInfo);
            }

            @Override
            public JSONObject companyInfoToJsonExport (TmpExport tmpExport) {
                return CompanyInfoMapper.super.companyInfoToJsonExport(tmpExport);
            }
        };
    }


}
