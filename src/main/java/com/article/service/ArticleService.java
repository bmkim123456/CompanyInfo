package com.article.service;

import com.article.entity.CompanyInfo;
import com.article.entity.NaverNewsItem;
import com.article.repository.CompanyInfoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ArticleService {


    private final CompanyInfoRepository companyInfoRepository;

    private final NaverNewsService naverNewsService;

    public ArticleService(CompanyInfoRepository companyInfoRepository, NaverNewsService naverNewsService) {
        this.companyInfoRepository = companyInfoRepository;
        this.naverNewsService = naverNewsService;
    }

    public List<NaverNewsItem> processUsersSequentially(int display) {
        Page<CompanyInfo> companyPage = companyInfoRepository.findAll(PageRequest.of(0, 10));
        List<CompanyInfo> companyInfos = companyPage.getContent();
        List<NaverNewsItem> naverNewsItems = new ArrayList<>();

        for (CompanyInfo companyInfo : companyInfos) {
            try {
                TimeUnit.SECONDS.sleep(2);
                String ceoName = companyInfo.getCeoName();
                String companyName = companyInfo.getCompanyName();

                // companyName 및 ceoName을 검색어로 네이버 뉴스 검색
                List<NaverNewsItem> newsItems = naverNewsService.searchNaverNews(companyName + " " + ceoName, display);
                naverNewsItems.addAll(newsItems);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                // InterruptedException 처리
                // 로깅 또는 예외를 다시 던지거나 에러 처리 방법에 따라 다른 값을 반환할 수 있음
                return Collections.emptyList();
            }
        }

        return naverNewsItems;
    }



    // 회사 정보를 가져오는 로직
    /*public String processUsersSequentially() {
        Page<CompanyInfo> companyPage = companyInfoRepository.findAll(PageRequest.of(0,10));
        List<CompanyInfo> companyInfos = companyPage.getContent();
        StringBuilder resultBuilder = new StringBuilder();

        for (CompanyInfo companyInfo : companyInfos) {
            try {
                TimeUnit.SECONDS.sleep(2);
                String jsonResult = "{\"id\":" + companyInfo.getId_seq() + ",\"회사명\":\"" + companyInfo.getCompanyName() + ",\"대표자\":\"" + companyInfo.getCeoName() + "\"}";
                System.out.println("Sending JSON: " + jsonResult);
                resultBuilder.append(jsonResult).append("\n");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Error";
            }
        }

        return resultBuilder.toString();
    }*/


}
