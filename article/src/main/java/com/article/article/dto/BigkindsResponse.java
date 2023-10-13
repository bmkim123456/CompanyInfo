package com.article.article.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

// 빅카인즈 기사 응답
@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BigkindsResponse {

    private int result;
    @JsonProperty("return_object")
    private ReturnObject returnObject;

    @Getter
    public static class ReturnObject {

        @JsonProperty("total_hits")
        private int totalHits;

        @Getter
        @JsonProperty("documents")
        private Document[] documents;

    }

    @Getter
    public static class Document {
        @JsonProperty("news_id")
        private String newsId;
        @JsonProperty("title")
        private String title;
        @JsonProperty("content")
        private String content;
        @JsonProperty("published_at")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        private LocalDateTime publishedAt;
        @JsonProperty("provider")
        private String provider;
        @JsonProperty("byline")
        private String byline;
        @JsonProperty("provider_link_page")
        private String providerLinkPage;

    }
}
