package com.article.service;

import com.article.entity.CompanyInfo;
import com.article.entity.Identified;
import com.article.entity.TmpExport;
import com.article.mapper.CompanyInfoMapper;
import com.article.repository.CompanyInfoRepository;
import com.article.repository.IdentifiedRepository;
import com.article.repository.TmpExportRepository;
import com.article.util.EncryptionUtil;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CompanySearchService {


    private final CompanyInfoRepository companyInfoRepository;
    private final RabbitTemplate rabbitTemplate;
    private final CompanyInfoMapper companyInfoMapper;
    private final IdentifiedRepository identifiedRepository;
    private final TmpExportRepository tmpExportRepository;
    private final EncryptionUtil encryptionUtil;

    public CompanySearchService(CompanyInfoRepository companyInfoRepository, RabbitTemplate rabbitTemplate, CompanyInfoMapper companyInfoMapper, IdentifiedRepository identifiedRepository, TmpExportRepository tmpExportRepository, EncryptionUtil encryptionUtil) {
        this.companyInfoRepository = companyInfoRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.companyInfoMapper = companyInfoMapper;
        this.identifiedRepository = identifiedRepository;
        this.tmpExportRepository = tmpExportRepository;
        this.encryptionUtil = encryptionUtil;
    }

    /** 회사 정보와 관련된 모든 기사내용을 수집할 때 사용할 로직 */
    public String processUsersSequentially() {

        Pageable pageable = PageRequest.of(0, 1000);
        Page<TmpExport> identifiedList = tmpExportRepository.findMatchingCompanies(pageable);
        StringBuilder resultBuilder = new StringBuilder();

        int pageIndex = 1;
        for (TmpExport tmpExport : identifiedList) {
            try {
                JSONObject jsonObject = companyInfoMapper.companyInfoToJsonExport(tmpExport);
                String jsonResult = jsonObject.toString();
                System.out.println("Sending JSON: " + jsonResult);

                String encryption = encryptionUtil.encrypt(jsonResult);

                pageIndex++;

                /** 큐로 기업정보 전달 */
                rabbitTemplate.convertAndSend("hubble.article.queue", encryption);
                resultBuilder.append(encryption).append("\n");

                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Error";
            }
        }

        return resultBuilder.toString();
    }

    /** 회사에서 발행된 기사가 있는지 없는지 여부를 체크할 경우 실행할 로직 (전수조사 x) */
    public String processUsersSequentiallyCnt() {

        Pageable pageable = PageRequest.of(1, 844012);
        Page<Identified> identifiedList = identifiedRepository.findMatchingCompanies(pageable);

        StringBuilder resultBuilder = new StringBuilder();

        int pageIndex = 845636;
        for (Identified identified : identifiedList) {
            try {
                JSONObject jsonObject = companyInfoMapper.companyInfoToJson(identified);
                jsonObject.put("pageIndex", pageIndex);
                String jsonResult = jsonObject.toString();
                System.out.println("Sending JSON: " + jsonResult);

                pageIndex++;

                // 큐로 기업정보 전달
                rabbitTemplate.convertAndSend("hubble.articlecnt.queue", jsonResult);
                resultBuilder.append(jsonResult).append("\n");

                /*// api로 기업정보 전달
                String postUrl = "http://localhost:8085/api/article/article";

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> requestEntity = new HttpEntity<>(jsonResult, headers);

                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> responseEntity = restTemplate.exchange(
                        postUrl,
                        HttpMethod.POST,
                        requestEntity,
                        String.class
                );*/

                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Error";
            }
        }

        return resultBuilder.toString();
    }

    public String sendYoutubeSearch() {

        Pageable pageable = PageRequest.of(0, 1000);
        Page<Identified> identifiedList = identifiedRepository.findMatchingCompanies(pageable);
        StringBuilder resultBuilder = new StringBuilder();

        int pageIndex = 1;
        for (Identified identified : identifiedList) {
            try {
                JSONObject jsonObject = companyInfoMapper.companyInfoToJson(identified);
                String jsonResult = jsonObject.toString();
                System.out.println("Sending JSON: " + jsonResult);

                String encryption = encryptionUtil.encrypt(jsonResult);

                pageIndex++;

                /** 큐로 기업정보 전달 */
                rabbitTemplate.convertAndSend("hubble.youtube.queue", encryption);
                resultBuilder.append(encryption).append("\n");

                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Error";
            }
        }

        return resultBuilder.toString();
    }



}
