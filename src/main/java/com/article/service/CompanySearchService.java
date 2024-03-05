package com.article.service;

import com.article.entity.CompanyInfo;
import com.article.repository.CompanyInfoRepository;
import com.article.util.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CompanySearchService {

    private final CompanyInfoRepository companyInfoRepository;
    private final RabbitTemplate rabbitTemplate;
    private final EncryptionUtil encryptionUtil;

    public List<JSONObject> convertToJsonObject () {
        List<CompanyInfo> companyInfoList = companyInfoRepository.findCompany();
        List<JSONObject> jsonObjects = new ArrayList<>();
        for (CompanyInfo companyInfo : companyInfoList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id_seq", companyInfo.getIdSeq());
            jsonObject.put("companyName", companyInfo.getCompanyName());
            if (companyInfo.getCeoName() == null) {
                jsonObject.put("ceoName", "");
            } else jsonObject.put("ceoName", companyInfo.getCeoName());
            jsonObjects.add(jsonObject);
        }
        return jsonObjects;
    }

    public String sendCompanyInfoUrl() {

        List<JSONObject> jsonObjects = convertToJsonObject();
        String postUrl = "http://49.247.9.151:30020/api/article/article_cnt";
        StringBuilder resultBuilder = new StringBuilder();

        for (JSONObject obj : jsonObjects) {
            try {
                String jsonResult = obj.toString();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> requestEntity = new HttpEntity<>(jsonResult, headers);

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.exchange(
                        postUrl,
                        HttpMethod.POST,
                        requestEntity,
                        String.class
                );

                resultBuilder.append(jsonResult).append("\n");

                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Error";
            }
        }
        return resultBuilder.toString();
    }

    public String sendToCompanyInfoQueue () {

        List<JSONObject> jsonObjects = convertToJsonObject();
        StringBuilder resultBuilder = new StringBuilder();

        for (JSONObject obj : jsonObjects) {
            try {
                String jsonResult = obj.toString();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                String encrypt = encryptionUtil.encrypt(jsonResult);

                rabbitTemplate.convertAndSend("hubble.article.queue", encrypt);

                resultBuilder.append(jsonResult).append("\n");

                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Error";
            }
        }
        return resultBuilder.toString();
    }

    public String sendToCompanyInfoQueueCnt () {

        List<JSONObject> jsonObjects = convertToJsonObject();
        StringBuilder resultBuilder = new StringBuilder();

        for (JSONObject obj : jsonObjects) {
            try {
                String jsonResult = obj.toString();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                String encrypt = encryptionUtil.encrypt(jsonResult);

                rabbitTemplate.convertAndSend("hubble.articlecnt.queue", encrypt);

                resultBuilder.append(jsonResult).append("\n");

                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Error";
            }
        }
        return resultBuilder.toString();
    }
}