package com.article.article.service;

import com.article.article.dto.BigkindsResponse;
import com.article.article.dto.CompanySearchParam;
import com.article.article.dto.NaverResponse;
import com.article.article.entity.LogRecord;
import com.article.article.repository.LogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {

    private final NaverArticleService naverArticleService;
    private final BigkindsArticleService bigkindsArticleService;
    private final LogRepository logRepository;
    private static final Logger log = LoggerFactory.getLogger(ArticleService.class);

    public ArticleService(NaverArticleService naverArticleService, BigkindsArticleService bigkindsArticleService, LogRepository logRepository) {
        this.naverArticleService = naverArticleService;
        this.bigkindsArticleService = bigkindsArticleService;
        this.logRepository = logRepository;
    }

    public void articleSearch (CompanySearchParam searchParam) throws JsonProcessingException {

        try {
            if (searchParam.getCeoName() == null) {

                // 대표자명이 null 이어서 수집 못한 기업들 별도 리스트 저장
                LogRecord logRecord = new LogRecord();
                logRecord.setIdSeq(searchParam.getId_seq());
                logRecord.setCompanyName(searchParam.getCompanyName());
                logRecord.setReason("대표자 이름 없음");

                logRepository.save(logRecord);

                log.warn("기업명 {} 의 대표자명이 불분명하여 수집을 중단 합니다.", searchParam.getCompanyName());
            } else if (searchParam.getTermination().equals("CLOSED")) {
                log.info("기업명 {} 은(는) 수집을 진행하지 않습니다. 이유 : CLOSED", searchParam.getCompanyName());
            } else if (searchParam.getCorporateStatus().equals("살아있는 등기") || searchParam.getCorporateStatus().equals("회생절차")
                    || searchParam.getCorporateStatus().equals("보전관리")) {


                NaverResponse naver = naverArticleService.searchNaverArticles(searchParam);
                Thread.sleep(1000);
                BigkindsResponse bigkinds = bigkindsArticleService.searchBigkindsArticle(searchParam);
                Thread.sleep(1000);

            } else log.info("기업명 {} 은(는) 사업 활동 중이 아닙니다. 사유 : {}", searchParam.getCompanyName(), searchParam.getCorporateStatus());

        } catch (RuntimeException e) {
            throw new RuntimeException("오류가 발생하여 수집을 중단합니다.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
