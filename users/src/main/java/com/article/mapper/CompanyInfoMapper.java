package com.article.mapper;

import com.article.entity.CompanyInfo;
import org.json.JSONObject;

public interface CompanyInfoMapper {

    default JSONObject companyInfoToJson (CompanyInfo companyInfo) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id_seq", companyInfo.getIdSeq());
        jsonObject.put("companyName", companyInfo.getCompanyName());
        jsonObject.put("ceoName", companyInfo.getCeoName());
        jsonObject.put("termination", companyInfo.getTermination());
        jsonObject.put("corporateStatus", companyInfo.getCorporateStatus());
        return jsonObject;

    }

}
