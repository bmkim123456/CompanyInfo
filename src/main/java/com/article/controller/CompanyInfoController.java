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

    @PostMapping(value = "/file", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> uploadFile (@RequestPart CompanyDto dto,
                                              @RequestPart MultipartFile file) throws IOException {
        String result = searchService.sendFileToCompanyInfoQueue(dto, file);
        return ResponseEntity.ok(result);
    }

}
