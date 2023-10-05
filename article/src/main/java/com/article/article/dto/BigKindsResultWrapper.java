package com.article.article.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BigKindsResultWrapper {

    private int result;

    @JsonProperty("return_object")
    private BigkindsResult bigkindsResult;

}
