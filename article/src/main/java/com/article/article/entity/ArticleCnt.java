package com.article.article.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "article_aggr", schema = "public")
public class ArticleCnt {

    @Id
    @Column(name = "id_seq")
    private int idSeq;

    @Column(name = "article_y")
    private int articleY;

    @Column(name = "article_m")
    private int articleM;

    @Column(name = "article_d")
    private int articleD;

    @Column(name = "article_ymd")
    private LocalDateTime articleYMD;

    @Column(name = "article_cnt")
    private int articleCnt;

}
