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
public class Naver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long article_seq;

    @Column
    private LocalDateTime create_datetime;

    @Column
    private LocalDateTime update_datetime;

    @Column
    private int id_seq;

    @Column
    private String Source;

    @Column
    private String news_id;

    @Column
    private String origin_link;

    @Column
    private String link;

    @Column
    private String title;

    @Column
    private String prev_content;



}
