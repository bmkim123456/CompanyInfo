package com.article.controller;


import com.article.dto.CompanyDto;
import com.article.service.CompanySearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CompanyInfoController {

    private final CompanySearchService searchService;


    @PostMapping("/searchUrl")
    public ResponseEntity<String> companyInfoUrl(@RequestBody CompanyDto dto) {

        String result = searchService.sendCompanyInfoUrl(dto);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/searchQueue")
    public ResponseEntity<String> companyInfoQueue(@RequestBody CompanyDto dto) {

        String result = searchService.sendToCompanyInfoQueue(dto);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/search/article_cnt")
    public ResponseEntity<String> companyInfoQueueCnt(@RequestBody CompanyDto dto) {
        String result = searchService.sendToCompanyInfoQueueCnt(dto);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/file", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> uploadFile (@RequestPart CompanyDto dto,
                                              @RequestPart MultipartFile file) throws IOException {
        String result = searchService.sendFileToCompanyInfoQueue(dto, file);
        return ResponseEntity.ok(result);
    }

}
