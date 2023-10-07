package com.article.article.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NaverResponse {

    @JsonProperty("lastBuildDate")
    private String lastBuildDate;
    @JsonProperty("total")
    private int total;
    @JsonProperty("start")
    private int start;
    @JsonProperty("display")
    private int display;

    @Getter
    @JsonProperty("items")
    private Items[] items;


    @Getter
    public static class Items {

        @JsonProperty("title")
        private String title;
        @JsonProperty("originallink")
        private String originalLink;
        @JsonProperty("link")
        private String link;
        @JsonProperty("description")
        private String description;
        @JsonProperty("pubDate")
        @JsonFormat(pattern = "EEE, dd MMM yyyy HH:mm:ss Z", locale = "en_US")
        private LocalDateTime pubDate;

    }
}
