package com.article.controller;


import com.article.dto.CompanyDto;
import com.article.service.CompanySearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
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
        if (ObjectUtils.isEmpty(dto.getType())) {
            return new ResponseEntity<>("type은 비어있을 수 없습니다", HttpStatus.BAD_REQUEST);
        } else if (!dto.getType().equals("키워드")) {
            return new ResponseEntity<>("type은 '키워드' 로 작성합니다.", HttpStatus.BAD_REQUEST);
        } else searchService.sendFileToCompanyInfoQueue(dto, file);
        return ResponseEntity.ok("조회 완료");
    }

    @PostMapping("/company")
    public ResponseEntity<String> findWholeCompany (@RequestBody CompanyDto dto) {
        if (ObjectUtils.isEmpty(dto.getType()) || ObjectUtils.isEmpty(dto.getRange())) {
            return new ResponseEntity<>("type과 range 항목은 비어있을 수 없습니다", HttpStatus.BAD_REQUEST);
        } else if (!dto.getType().equals("전체수집") && !dto.getType().equals("키워드") && !dto.getType().equals("기사개수")) {
            return new ResponseEntity<>("type은 다음 3가지만 요청만 가능합니다 : 1.전체수집, 2.키워드, 3.기사개수", HttpStatus.BAD_REQUEST);
        } else if (!dto.getRange().equals("전체") && !dto.getRange().equals("일부")) {
            return new ResponseEntity<>("range는 '전체', '일부' 만 입력 가능합니다.", HttpStatus.BAD_REQUEST);
        } else searchService.sendCompanyInfo(dto.getType(), dto.getRange());
        return ResponseEntity.ok("조회 완료");
    }

}
