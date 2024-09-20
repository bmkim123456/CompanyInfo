package com.article.mongo_test;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Getter
@Document(value="mapped_coop")
public class Mapped {

    @Id
    private String id;


}
