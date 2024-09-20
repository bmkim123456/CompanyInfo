package com.article.service;

import com.article.repository.CompanyInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanySendService {

    private final CompanyInfoRepository companyInfoRepository;
    private final RestTemplate restTemplate;

    @Transactional
    public void sendEnr (String header, String enr) {

        createHeaders(header).setContentType(MediaType.APPLICATION_JSON);
        createHeaders(header).setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Map<String, String> body = Collections.singletonMap("enr", enr);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, createHeaders(header));
        String url = "http://221.168.32.254:17210/company/detail";

        try {
            restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            log.info("Request Enr :  {}" ,enr);
        } catch (HttpClientErrorException e) {
            System.err.println("Client error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        }
    }

    @Transactional
    public void sendEnrList (String header) {

        List<String> companyInfoList = companyInfoRepository.getCompanyEnr().collect(Collectors.toList());

        try {
            for (String enr : companyInfoList) {
                HttpHeaders headers = createHeaders(header);
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

                Map<String, String> body = Collections.singletonMap("enr", enr);
                HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
                String url = "https://api.hubble.co.kr/company/detail";

                try {
                    restTemplate.exchange(url, HttpMethod.POST, request, String.class);
                    log.info("Request Enr : {}" ,enr);
                } catch (HttpClientErrorException e) {
                    System.err.println("Client error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
                }

                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            log.error("기타 에러 : " + e);
        }
    }

    private HttpHeaders createHeaders(String header) {
        HttpHeaders headers = new HttpHeaders();

        String accessKey = extractValue(header, "access_key");
        String signedDate = extractValue(header, "signed_date");
        String method = extractValue(header, "method");
        String path = extractValue(header, "path");
        String signature = extractValue(header, "signature");

        String headerValue = String.format("access_key=%s signed_date=%s method=%s path=%s signature=%s",
                accessKey, signedDate, method, path, signature);
        headers.set("Authorization", headerValue);
        return headers;
    }

    private String extractValue(String header, String key) {
        String[] parts = header.split(" ");
        for (String part : parts) {
            if (part.startsWith(key)) {
                return part.split("=")[1];
            }
        }
        return null;
    }



}
