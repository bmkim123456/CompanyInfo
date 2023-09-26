package com.article.service;

import com.article.entity.CompanyInfo;
import com.article.repository.CompanyInfoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ArticleService {

    private final CompanyInfoRepository testRepository;

    public ArticleService(CompanyInfoRepository testRepository) {
        this.testRepository = testRepository;
    }

    // 회사 정보를 가져오는 로직

    public String processUsersSequentially() {
        Page<CompanyInfo> testPage = testRepository.findAll(PageRequest.of(0,10));
        List<CompanyInfo> articles = testPage.getContent();
        StringBuilder resultBuilder = new StringBuilder();

        for (CompanyInfo article : articles) {
            try {
                TimeUnit.SECONDS.sleep(2);
                String jsonResult = "{\"id\":" + article.getId_seq() + ",\"회사명\":\"" + article.getCompanyName() + ",\"대표자\":\"" + article.getCeoName() + "\"}";
                System.out.println("Sending JSON: " + jsonResult);
                resultBuilder.append(jsonResult).append("\n");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Error";
            }
        }

        return resultBuilder.toString();
    }


}
