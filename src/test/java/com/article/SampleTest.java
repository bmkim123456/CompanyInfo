package com.article;

import com.article.study.BadWord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@ActiveProfiles("local")
public class SampleTest {

    @Autowired
    BadWord badWord;

    @Test
    public void sortTest () {

        List<String> sortList = List.of("BBB", "AA", "A", "AAA", "BB", "C", "CCC", "CC", "B");

        List<String> sortListTemp = sortList.stream().sorted().collect(Collectors.toList());
        for(String sort : sortListTemp) {
            System.out.println(sort);
        }
    }


    @Test
    void doFilter () {

        String text = "욕설로 정의된 단어 : 씨발, 개새끼, 염병";
        String result = badWord.doFilter(text);
        System.out.println(result);
    }


}
