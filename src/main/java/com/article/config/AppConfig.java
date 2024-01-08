package com.article.config;

import com.article.util.EncryptionUtil;
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


}
