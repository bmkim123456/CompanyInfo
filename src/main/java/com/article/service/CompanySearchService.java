package com.article.service;

import com.article.dto.CompanyDto;
import com.article.entity.CompanyInfo;
import com.article.repository.CompanyInfoRepository;
import com.article.util.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanySearchService {

    private final CompanyInfoRepository companyInfoRepository;
    private final RabbitTemplate rabbitTemplate;
    private final EncryptionUtil encryptionUtil;

    public List<JSONObject> convertFileToJsonObject (CompanyDto dto, MultipartFile file) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
        List<CompanyInfo> companyInfoList = new ArrayList<>();
        String line;
        while (!ObjectUtils.isEmpty((line = br.readLine()))) {
            if (line.contains("id_seq")) {
                continue;
            }
            Long id = Long.parseLong(line.trim().replace(" ", ""));
            CompanyInfo entity = companyInfoRepository.getCompanyInfo(id);
            if (!ObjectUtils.isEmpty(entity)) {
                companyInfoList.add(entity);
            } else System.out.println("존재하지 않는 ID : " + id);
        }
        br.close();
        List<JSONObject> jsonObjects = new ArrayList<>();
        for (CompanyInfo companyInfo : companyInfoList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("idSeq", companyInfo.getIdSeq());
            jsonObject.put("companyName", companyInfo.getCompanyName());
            if (companyInfo.getCeoName() == null) {
                jsonObject.put("ceoName", "");
            } else jsonObject.put("ceoName", companyInfo.getCeoName());
            jsonObject.put("keyword", dto.getKeyword());
            jsonObject.put("requestDate", dto.getRequestDate());
            jsonObjects.add(jsonObject);
        }
        return jsonObjects;
    }

    public String sendFileToCompanyInfoQueue (CompanyDto dto, MultipartFile file) throws IOException {

        List<JSONObject> jsonObjects = convertFileToJsonObject(dto, file);
        StringBuilder resultBuilder = new StringBuilder();

        for (JSONObject obj : jsonObjects) {
            try {
                String jsonResult = obj.toString();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                String encrypt = encryptionUtil.encrypt(jsonResult);

                rabbitTemplate.convertAndSend("ibk.article", encrypt);

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