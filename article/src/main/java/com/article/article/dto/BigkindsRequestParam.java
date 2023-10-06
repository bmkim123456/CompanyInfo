package com.article.article.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class BigkindsRequestParam {

    @JsonProperty("query")
    private String keyword;
    @JsonProperty("published_at")
    private PublishedAt publishedAt;
    @JsonProperty("sort")
    private Sort sort;
    @JsonProperty("return_from")
    private int returnFrom;
    @JsonProperty("return_size")
    private int returnSize;
    @JsonProperty("access_key")
    private String accessKey;

    @JsonProperty("argument")
    private Map<String, Object> argument;
    private List<String> fields;

    public BigkindsRequestParam(String keyword) {
        this.keyword = keyword;
        this.publishedAt = new PublishedAt();
        this.sort = new Sort("desc");
        this.returnFrom = 0;
        this.returnSize = 100;
        // 반환할 필드 지정
        this.fields = List.of( "news_id", "published_at", "title", "content",
                "provider", "byline", "category", "category_incident",
                "provider_link_page", "printing_page");
        this.accessKey = "661f02bd-536b-45de-adf2-e3a0764f0ffb";

        Map<String, Object> argument = new HashMap<>();
        argument.put("query", keyword);
        argument.put("published_at", this.publishedAt);
        argument.put("sort", this.sort);
        argument.put("return_from", this.returnFrom);
        argument.put("return_size", this.returnSize);
        argument.put("fields", this.fields);

        this.argument = argument;
    }

    public void setStartDate(LocalDate minDate) {
        this.publishedAt.from = minDate.toString();
    }

    @Data
    class PublishedAt {
        private String from;
        private String until;

        public PublishedAt() {
            this.from = "2021-01-01";
            this.until = LocalDate.now().toString();
        }
    }

    @Data
    class Sort {
        private String sort;

        public Sort(String sort) {
            this.sort = sort;
        }
    }


}

