package com.article.article.service;

import com.article.article.dto.CompanySearchParam;
import com.article.article.entity.Naver;
import com.article.article.repository.NaverRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class NaverService {

    @Value("${naver.api.clientId}")
    private String clientId;

    @Value("${naver.api.clientSecret}")
    private String clientSecret;

    private final NaverRepository naverRepository;

    public NaverService(NaverRepository naverRepository) {
        this.naverRepository = naverRepository;
    }


    public void saveNaverNews (Naver naver) {
        naverRepository.save(naver);
    }

    private void process (CompanySearchParam companySearchParam) {

        final int idSeq = companySearchParam.getId_seq();
        final LocalDateTime minDate = companySearchParam.getArticleUpdateDatetime();

        final List<String> keywords = new ArrayList<>();
        keywords.add(String.format("%s %s", companySearchParam.getCompanyName(), companySearchParam.getCeoName()));
    }
}
