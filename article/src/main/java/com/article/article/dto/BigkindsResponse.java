package com.article.article.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BigkindsResponse {

    private int result;
    @JsonProperty("return_object")
    private ReturnObject returnObject;

    // getter 및 setter 생략

    public static class ReturnObject {
        @JsonProperty("total_hits")
        private int totalHits;
        @JsonProperty("documents")
        @Getter
        private Document[] documents;

        // getter 및 setter 생략
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
        private String publishedAt;
        @JsonProperty("provider")
        private String provider;
        @JsonProperty("byline")
        private String byline;
        @JsonProperty("provider_link_page")
        private String providerLinkPage;

    }
}
