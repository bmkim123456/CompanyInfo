package com.article.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "temp_id", schema = "data")
public class AttentionCompany {

    @Id
    @Column(name = "id_seq")
    private Long idSeq;

}
