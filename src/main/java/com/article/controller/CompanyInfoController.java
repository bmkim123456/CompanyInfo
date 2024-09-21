package com.article.controller;


import com.article.dto.CompanyDto;
import com.article.service.CompanySearchService;
import com.article.service.CompanySendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyInfoController {

    private final CompanySearchService searchService;
    private final CompanySendService sendService;

    @PostMapping(value = "/file", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> uploadCompanyPartFile (@RequestPart CompanyDto dto,
                                              @RequestPart MultipartFile file) {
       try {
           searchService.sendFileToArticlePartTable(dto, file);
           return ResponseEntity.ok("조회 완료");
       } catch (Exception e) {
           return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
       }
    }

    @PostMapping(value = "/whole", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> wholeCompanyInfo () {
        try {
            searchService.sendCompanyInfoToArticleTable();
            return ResponseEntity.ok("조회 완료");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/part")
    public ResponseEntity<String> partCompanyInfo (@RequestBody CompanyDto dto) {
        try {
            searchService.sendCompanyInfoToArticlePart(dto);
            return ResponseEntity.ok("조회 완료");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/sendEnr")
    public ResponseEntity<String> sendDataApi(@RequestHeader("Authorization") String authHeader, @RequestBody String enr) {
        sendService.sendEnr(authHeader, enr);
        return ResponseEntity.ok("전달성공");
    }

    @GetMapping("/sendEnrList")
    public ResponseEntity<String> sendDataApi2(@RequestHeader("Authorization") String authHeader) {
        sendService.sendEnrList(authHeader);
        return ResponseEntity.ok("전달성공");
    }

}
