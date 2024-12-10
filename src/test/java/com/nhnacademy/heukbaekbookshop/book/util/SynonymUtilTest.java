package com.nhnacademy.heukbaekbookshop.book.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SynonymUtilTest {

    @Test
    void testGetSynonyms_KnownWord() {
        // Given
        String keyword = "자바";

        // When
        List<String> synonyms = SynonymUtil.getSynonyms(keyword);

        // Then
        assertNotNull(synonyms);
        assertEquals(1, synonyms.size());
        assertTrue(synonyms.contains("java"));
    }

    @Test
    void testGetSynonyms_KnownWord_Bidirectional() {
        // Given
        String keyword = "java";

        // When
        List<String> synonyms = SynonymUtil.getSynonyms(keyword);

        // Then
        assertNotNull(synonyms);
        assertEquals(1, synonyms.size());
        assertTrue(synonyms.contains("자바"));
    }

    @Test
    void testGetSynonyms_UnknownWord() {
        // Given
        String keyword = "알 수 없는 단어";

        // When
        List<String> synonyms = SynonymUtil.getSynonyms(keyword);

        // Then
        assertNotNull(synonyms);
        assertTrue(synonyms.isEmpty());
    }

    @Test
    void testGetSynonyms_AllKnownWords() {
        // Given
        String[] keywords = {"자바", "학생", "구입", "예쁜", "슬픈", "기질", "LA"};

        // When & Then
        for (String keyword : keywords) {
            List<String> synonyms = SynonymUtil.getSynonyms(keyword);
            assertNotNull(synonyms);
            assertEquals(1, synonyms.size());
            assertFalse(synonyms.contains(keyword)); // 동의어 목록에 자기 자신이 포함되지 않음

            // 추가적인 검증: 반대 방향도 확인
            String synonym = synonyms.get(0);
            List<String> reverseSynonyms = SynonymUtil.getSynonyms(synonym);
            assertNotNull(reverseSynonyms);
            assertEquals(1, reverseSynonyms.size());
            assertTrue(reverseSynonyms.contains(keyword));
        }
    }

    @Test
    void testGetSynonyms_CaseSensitivity() {
        // Given
        String keywordLower = "java";
        String keywordUpper = "JAVA";

        // When
        List<String> synonymsLower = SynonymUtil.getSynonyms(keywordLower);
        List<String> synonymsUpper = SynonymUtil.getSynonyms(keywordUpper);

        // Then
        // 대소문자를 구분하는 경우
        assertNotNull(synonymsLower);
        assertEquals(1, synonymsLower.size());
        assertTrue(synonymsLower.contains("자바"));

        // 대소문자를 구분하여 대문자로 검색 시 결과 없음
        assertNotNull(synonymsUpper);
        assertTrue(synonymsUpper.isEmpty());
    }
}
