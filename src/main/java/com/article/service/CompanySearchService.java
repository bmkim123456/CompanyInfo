package com.article.service;

import com.article.dto.CompanyDto;
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

import java.util.ArrayList;
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

    public String sendCompanyToRabbitMQ () {

        List<CompanyDto> companyDtoList = getCompany();
        StringBuilder resultBuilder = new StringBuilder();

        for (CompanyDto dto : companyDtoList) {
            JSONObject companyInfo = companyInfoToJson(dto);
            String result = companyInfo.toString();
            String encryption = encryptionUtil.encrypt(result);

            rabbitTemplate.convertAndSend("kised-report", encryption);
            System.out.println(result);
            resultBuilder.append(encryption).append("\n");
        }
        return resultBuilder.toString();
    }

    public List<CompanyDto> getCompany () {
        List<Object[]> resultList = companyInfoRepository.getCompanyInfos();
        List<CompanyDto> companyDtoList = convertToCompany(resultList);
        return companyDtoList;
    }

    private List<CompanyDto> convertToCompany (List<Object[]> resultList) {
        List<CompanyDto> companyDtoList = new ArrayList<>();
        for (Object[] result : resultList) {
            CompanyDto dto = new CompanyDto();
            dto.setIdSeq((Integer) result[0]);
            dto.setCompanyName((String) result[1]);
            dto.setCeoName((String) result[2]);
            companyDtoList.add(dto);
        }
        return companyDtoList;
    }

    private JSONObject companyInfoToJson (CompanyDto dto) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id_seq", dto.getIdSeq());
        jsonObject.put("companyName", dto.getCompanyName());
        jsonObject.put("ceoName", dto.getCeoName());
        return jsonObject;
    }



    /** 회사 정보와 관련된 모든 기사내용을 수집할 때 사용할 로직 */
    /*public String processUsersSequentially() {

        Pageable pageable = PageRequest.of(0, 1);
        Page<CompanyInfo> identifiedList = companyInfoRepository.findMatchingCompanies(pageable);
        StringBuilder resultBuilder = new StringBuilder();

        int pageIndex = 1;
        for (CompanyInfo companyInfo : identifiedList) {
            try {
                JSONObject jsonObject = companyInfoMapper.companyInfoToJsonExport(companyInfo);
                String jsonResult = jsonObject.toString();
                System.out.println("Sending JSON: " + jsonResult);

                String encryption = encryptionUtil.encrypt(jsonResult);

                pageIndex++;

                *//** 큐로 기업정보 전달 *//*
                //rabbitTemplate.convertAndSend("kt-report", encryption);
                resultBuilder.append(encryption).append("\n");

                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Error";
            }
        }

        return resultBuilder.toString();
    }*/

}
