package com.article.article.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "naver_article", schema = "public")
public class Naver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_seq")
    private Long article_seq;

    @Column(name = "create_datetime")
    private LocalDateTime create_datetime;

    @Column(name = "update_datetime")
    private LocalDateTime update_datetime;

    @Column(name = "id_seq")
    private int id_seq;

    @Column(name = "Source")
    private String Source;

    @Column(name = "news_id")
    private String news_id;

    @Column(name = "originLink")
    private String originLink;

    @Column(name = "link")
    private String link;

    @Column(name = "title")
    private String title;

    @Column(name = "prev_content")
    private String prev_content;



}
