package com.article.article.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class BigkindsRequestParam {

    @JsonProperty("query")
    private String keyword;
    @JsonProperty("published_at")
    private PublishedAt publishedAt;
    private Sort sort;
    @JsonProperty("return_from")
    private int returnFrom;
    @JsonProperty("return_size")
    private int returnSize;
    private List<String> fields;

    public BigkindsRequestParam(String keyword) {
        this.keyword = keyword;
        this.publishedAt = new PublishedAt();
        this.sort = new Sort("desc");
        this.returnFrom = 0;
        this.returnSize = 100;
        // 반환할 필드 지정
        this.fields = List.of("byline", "dateline", "news_id",
                "provider", "provider_link_page", "title", "content", "images");
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

