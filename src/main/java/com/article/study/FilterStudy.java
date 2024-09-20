package com.article.study;


import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public abstract class FilterStudy {

    abstract List<String> badWordList ();

    private List<String> badWordsList () {
        List<String> badWords = new ArrayList<>();
        badWords.add("씨발");
        badWords.add("개새끼");
        badWords.add("우라질");
        badWords.addAll(badWordList());
        return badWords;
    }

    public String doFilter (String word) {

        List<String> wrongWords = badWordsList();

        for (String bad : wrongWords) {
            if (word.contains(bad)) {
                word = word.replace(bad, "**");
            }
        }

        return word;
    }



    // ops-idc-db01, pg-svr-01
    // ops-idc-db02, pg-svr-02

}
