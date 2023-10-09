package com.article.article.service;

import com.article.article.dto.BigkindsResponse;
import com.article.article.dto.CompanySearchParam;
import com.article.article.dto.NaverResponse;
import com.article.article.exception.SearchParamException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {

    private final NaverArticleService naverArticleService;
    private final BigkindsArticleService bigkindsArticleService;
    private static final Logger log = LoggerFactory.getLogger(ArticleService.class);

    public ArticleService(NaverArticleService naverArticleService, BigkindsArticleService bigkindsArticleService) {
        this.naverArticleService = naverArticleService;
        this.bigkindsArticleService = bigkindsArticleService;
    }

    public void articleSearch (CompanySearchParam searchParam) throws JsonProcessingException {
        try {
            if (searchParam.getCeoName().isEmpty()) {
                log.warn("{} 회사의 대표자명을 알 수 없습니다.", searchParam.getCompanyName());
            } else if (searchParam.getTermination().equals("CLOSED")) {
                log.info("기업명 {} 은(는) 수집을 진행하지 않습니다. 이유 : CLOSED", searchParam.getCompanyName());
            } else if (searchParam.getCorporateStatus().equals("살아있는 등기") || searchParam.getCorporateStatus().equals("회생절차")
                    || searchParam.getCorporateStatus().equals("보전관리")) {

                NaverResponse naver = naverArticleService.searchNaverArticles(searchParam);
                BigkindsResponse bigkinds = bigkindsArticleService.searchBigkindsArticle(searchParam);

            } else log.info("기업명 {} 은(는) 사업 활동 중이 아닙니다.", searchParam.getCompanyName());
        } catch (RuntimeException e) {
            throw new SearchParamException("오류가 발생하여 수집을 중단합니다.", searchParam.getId_seq(), searchParam.getCompanyName());
        }
    }
}
