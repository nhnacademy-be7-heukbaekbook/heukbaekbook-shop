package com.nhnacademy.heukbaekbookshop.book.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SynonymUtil {
    private static final Map<String, List<String>> SYNONYM_MAP = new HashMap<>();

    static {
        addSynonyms("자바", "java");
        addSynonyms("학생", "제자");
        addSynonyms("구입", "구매");
        addSynonyms("예쁜", "아름다운");
        addSynonyms("슬픈", "우울한");
        addSynonyms("기질", "특성");
        addSynonyms("LA", "로스엔젤레스");
    }

    private static void addSynonyms(String word1, String word2) {
        SYNONYM_MAP.putIfAbsent(word1, List.of(word2));
        SYNONYM_MAP.putIfAbsent(word2, List.of(word1));
    }

    public static List<String> getSynonyms(String keyword) {
        return SYNONYM_MAP.getOrDefault(keyword, List.of());
    }
}
