package com.article.mapper;

import com.article.entity.CompanyInfo;
import com.article.entity.Identified;
import com.article.entity.TmpExport;
import org.json.JSONObject;

public interface CompanyInfoMapper {

    default JSONObject companyInfoToJson (CompanyInfo companyInfo) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id_seq", companyInfo.getIdSeq());
        jsonObject.put("companyName", companyInfo.getCompanyName());
        jsonObject.put("ceoName", companyInfo.getCeoName());
        return jsonObject;

    }

    default JSONObject companyInfoToJsonExport (TmpExport tmpExport) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id_seq", tmpExport.getIdSeq());
        jsonObject.put("companyName", tmpExport.getCompanyName());
        jsonObject.put("ceoName", tmpExport.getCeoName());
        return jsonObject;

    }

}
