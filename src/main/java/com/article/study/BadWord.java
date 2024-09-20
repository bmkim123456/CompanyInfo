package com.article.study;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BadWord extends FilterStudy {

    @Override
    List<String> badWordList() {
        return List.of("염병", "fuck");
    }
}
