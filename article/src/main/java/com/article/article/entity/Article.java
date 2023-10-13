package com.article.article.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

// 뉴스 데이터 저장할 내용
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "naver_article", schema = "public")
public class Article implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_seq")
    private Long articleSeq;

    @Column(name = "create_datetime")
    private LocalDateTime createDatetime;

    @Column(name = "update_datetime")
    private LocalDateTime updateDatetime;

    @Column(name = "id_seq")
    private int idSeq;

    @Column(name = "source")
    private String source;

    @Column(name = "news_id")
    private String newsId;

    @Column(name = "originLink")
    private String originLink;

    @Column(name = "link")
    private String link;

    @Column(name = "publish_datetime")
    private LocalDateTime publishDatetime;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "title")
    private String title;

    @Column(name = "prev_content")
    private String prevContent;

    @Column(name = "full_contents")
    private String fullContents;

    @Column(name = "author")
    private String author;

    @Column(name = "images")
    private String images;
}
