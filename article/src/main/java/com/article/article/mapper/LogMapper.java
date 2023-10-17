package com.article.article.mapper;

import com.article.article.dto.CompanySearchParam;
import com.article.article.entity.LogRecord;
import org.mapstruct.Mapper;

@Mapper
public interface LogMapper {

    default LogRecord companySearchParamToLogRecord (CompanySearchParam companySearchParam) {

        LogRecord logRecord = new LogRecord();
        logRecord.setIdSeq(companySearchParam.getId_seq());
        logRecord.setCompanyName(companySearchParam.getCompanyName());
        logRecord.setReason("대표자 이름 없음");
        return logRecord;

    }

}
