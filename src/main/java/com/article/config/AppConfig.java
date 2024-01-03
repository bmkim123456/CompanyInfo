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
            public JSONObject companyInfoToJson(Identified identified) {
                return CompanyInfoMapper.super.companyInfoToJson(identified);
            }

            @Override
            public JSONObject companyInfoToJsonExport (CompanyInfo companyInfo) {
                return CompanyInfoMapper.super.companyInfoToJsonExport(companyInfo);
            }
        };
    }


}
