package com.article.article.service;

import com.article.article.component.SearchResultsProducer;
import com.article.article.dto.CompanySearchParam;
import com.article.article.entity.Article;
import com.article.article.entity.ArticleCnt;
import com.article.article.entity.LogRecord;
import com.article.article.mapper.ArticleMapper;
import com.article.article.mapper.LogMapper;
import com.article.article.repository.ArticleCntRepository;
import com.article.article.repository.LogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ArticleService {

    private final NaverArticleService naverArticleService;
    private final BigkindsArticleService bigkindsArticleService;
    private final ArticleMapper articleMapper;
    private final ArticleCntRepository articleCntRepository;
    private static final Logger log = LoggerFactory.getLogger(ArticleService.class);


    public ArticleService(NaverArticleService naverArticleService, BigkindsArticleService bigkindsArticleService, ArticleMapper articleMapper,
                          ArticleCntRepository articleCntRepository) {
        this.naverArticleService = naverArticleService;
        this.bigkindsArticleService = bigkindsArticleService;
        this.articleMapper = articleMapper;
        this.articleCntRepository = articleCntRepository;
    }

    public void articleSearch (CompanySearchParam searchParam) throws JsonProcessingException {

        try {
            List<Article> naverArticle = naverArticleService.searchNaverArticles(searchParam);
            List<Article> bigkindsArticle = bigkindsArticleService.searchBigkindsArticle(searchParam);
            Thread.sleep(500);

            List<ArticleCnt> articleCntList = new ArrayList<>();
            List<Article> combinedList = new ArrayList<>(Stream.concat(bigkindsArticle.stream(), naverArticle.stream())
                           .collect(Collectors.toMap(
                                   article -> article.getTitle() + article.getOriginLink(),
                                   article -> article,
                                   (existing, replacement) -> existing
                           )).values());

            int count = 0;
            for (Article article : combinedList) {
                ArticleCnt existingArticleCnt = articleCntList.stream()
                               .filter(articleCnt -> articleCnt.getArticleYMD().isEqual(article.getPublishDatetime()))
                               .findFirst()
                               .orElse(null);

                if (existingArticleCnt == null) {
                    // 수집한 목록과 DB를 모두 체크했을 때 최초 기사일 경우 신규 데이터 생성
                    if (!isDuplicateArticleCnt(article.getIdSeq(), article.getPublishDatetime())) {
                        ArticleCnt articleCnt = articleMapper.searchArticleCnt(article, searchParam);
                        articleCntList.add(articleCnt);
                        count += 1;
                        // 수집한 목록에는 신규 날짜지만 기존 DB에 이미 기사가 발행 된 이력이 있을 경우 기존 데이터 cnt 컬럼에 카운트 +1
                    } else {
                        ArticleCnt articleCnt = articleCntRepository.findByIdSeqAndArticleYMD(article.getIdSeq(), article.getPublishDatetime());
                        articleCnt.setArticleCnt(articleCnt.getArticleCnt() + 1);
                        articleCntRepository.save(articleCnt);
                        count += 1;
                    }
                    // 같은 날짜에 발행된 다른 기사라면 cnt 컬럼에 +1
                } else {
                    existingArticleCnt.setArticleCnt(existingArticleCnt.getArticleCnt() + 1);
                    count += 1;
                }

            }

            Collections.sort(articleCntList);
            log.info("전체 {}건 수집되었습니다.", count);

            // searchResultsProducer.sendSearchResults(combinedList);
            // searchResultsProducer.sendArticleCntResult(articleCntList);
            Thread.sleep(500);
            articleCntRepository.saveAll(articleCntList);
                   combinedList.clear();
                   articleCntList.clear();

        } catch (RuntimeException e) {
            throw new RuntimeException("오류가 발생하여 수집을 중단합니다.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private boolean isDuplicateArticleCnt(Integer idSeq, LocalDate articleYMD) {
        return articleCntRepository.existsByIdSeqAndArticleYMD(idSeq, articleYMD);
    }

}
