package com.nhnacademy.heukbaekbookshop.order.service.impl;

import com.nhnacademy.heukbaekbookshop.order.domain.WrappingPaper;
import com.nhnacademy.heukbaekbookshop.image.domain.WrappingPaperImage;
import com.nhnacademy.heukbaekbookshop.order.dto.response.WrappingPaperResponse;
import com.nhnacademy.heukbaekbookshop.order.repository.WrappingPaperRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WrappingPaperServiceImplTest {

    @Mock
    private WrappingPaperRepository wrappingPaperRepository;

    @InjectMocks
    private WrappingPaperServiceImpl wrappingPaperService;

    @Test
    @DisplayName("getAllWrappingPapers - 성공적으로 모든 포장지 조회")
    void testGetAllWrappingPapers_Success() {
        // Given
        WrappingPaperImage image1 = new WrappingPaperImage();
        image1.setUrl("http://example.com/image1.jpg");

        WrappingPaper wrappingPaper1 = new WrappingPaper();
        wrappingPaper1.setId(1L);
        wrappingPaper1.setName("Red Wrapping Paper");
        wrappingPaper1.setPrice(new BigDecimal("5000"));
        wrappingPaper1.setWrappingPaperImage(image1);

        WrappingPaperImage image2 = new WrappingPaperImage();
        image2.setUrl("http://example.com/image2.jpg");

        WrappingPaper wrappingPaper2 = new WrappingPaper();
        wrappingPaper2.setId(2L);
        wrappingPaper2.setName("Blue Wrapping Paper");
        wrappingPaper2.setPrice(new BigDecimal("6000"));
        wrappingPaper2.setWrappingPaperImage(image2);

        List<WrappingPaper> wrappingPapers = Arrays.asList(wrappingPaper1, wrappingPaper2);
        when(wrappingPaperRepository.searchAll()).thenReturn(wrappingPapers);

        // When
        List<WrappingPaperResponse> responses = wrappingPaperService.getAllWrappingPapers();

        // Then
        verify(wrappingPaperRepository, times(1)).searchAll();
        assertNotNull(responses, "응답은 null이 아니어야 합니다.");
        assertEquals(2, responses.size(), "응답 리스트의 크기가 일치해야 합니다.");

        WrappingPaperResponse response1 = responses.get(0);
        assertEquals(1L, response1.id(), "첫 번째 포장지 ID가 일치해야 합니다.");
        assertEquals("Red Wrapping Paper", response1.name(), "첫 번째 포장지 이름이 일치해야 합니다.");
        assertEquals(new BigDecimal("5000"), response1.price(), "첫 번째 포장지 가격이 일치해야 합니다.");
        assertEquals("http://example.com/image1.jpg", response1.imageUrl(), "첫 번째 포장지 이미지 URL이 일치해야 합니다.");

        WrappingPaperResponse response2 = responses.get(1);
        assertEquals(2L, response2.id(), "두 번째 포장지 ID가 일치해야 합니다.");
        assertEquals("Blue Wrapping Paper", response2.name(), "두 번째 포장지 이름이 일치해야 합니다.");
        assertEquals(new BigDecimal("6000"), response2.price(), "두 번째 포장지 가격이 일치해야 합니다.");
        assertEquals("http://example.com/image2.jpg", response2.imageUrl(), "두 번째 포장지 이미지 URL이 일치해야 합니다.");
    }

    @Test
    @DisplayName("getAllWrappingPapers - 포장지 목록이 비어있는 경우")
    void testGetAllWrappingPapers_EmptyList() {
        // Given
        when(wrappingPaperRepository.searchAll()).thenReturn(Collections.emptyList());

        // When
        List<WrappingPaperResponse> responses = wrappingPaperService.getAllWrappingPapers();

        // Then
        verify(wrappingPaperRepository, times(1)).searchAll();
        assertNotNull(responses, "응답은 null이 아니어야 합니다.");
        assertTrue(responses.isEmpty(), "응답 리스트는 비어있어야 합니다.");
    }

    @Test
    @DisplayName("getAllWrappingPapers - 포장지 조회 중 예외 발생")
    void testGetAllWrappingPapers_Exception() {
        // Given
        when(wrappingPaperRepository.searchAll()).thenThrow(new RuntimeException("Database error"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            wrappingPaperService.getAllWrappingPapers();
        }, "예외가 발생해야 합니다.");

        assertEquals("Database error", exception.getMessage(), "예외 메시지가 일치해야 합니다.");
        verify(wrappingPaperRepository, times(1)).searchAll();
    }
}