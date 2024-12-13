package com.nhnacademy.heukbaekbookshop.point.earn.service.impl;

import com.nhnacademy.heukbaekbookshop.point.earn.domain.*;
import com.nhnacademy.heukbaekbookshop.point.earn.dto.request.PointEarnStandardRequest;
import com.nhnacademy.heukbaekbookshop.point.earn.dto.response.PointEarnStandardResponse;
import com.nhnacademy.heukbaekbookshop.point.earn.exception.PointEarnEventNotFoundException;
import com.nhnacademy.heukbaekbookshop.point.earn.exception.PointEarnStandardIdNotFoundException;
import com.nhnacademy.heukbaekbookshop.point.earn.repository.PointEarnEventRepository;
import com.nhnacademy.heukbaekbookshop.point.earn.repository.PointEarnStandardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PointEarnStandardServiceImplTest {

    @InjectMocks
    private PointEarnStandardServiceImpl pointEarnStandardService;

    @Mock
    private PointEarnStandardRepository pointEarnStandardRepository;

    @Mock
    private PointEarnEventRepository pointEarnEventRepository;

    @Test
    void testCreatePointEarnStandard_Success() {
        // Given
        PointEarnStandardRequest request = new PointEarnStandardRequest(
                "New Standard",
                BigDecimal.valueOf(100),
                PointEarnType.FIXED,
                PointEarnStandardStatus.ACTIVATED,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30),
                1L
        );

        PointEarnEvent mockEvent = new PointEarnEvent();
        mockEvent.setId(1L);
        mockEvent.setEventCode(EventCode.SIGNUP);

        PointEarnStandard mockStandard = new PointEarnStandard(
                1L,
                "New Standard",
                BigDecimal.valueOf(100),
                PointEarnType.FIXED,
                PointEarnStandardStatus.ACTIVATED,
                request.pointEarnStart(),
                request.pointEarnEnd(),
                mockEvent
        );

        when(pointEarnEventRepository.findById(request.pointEarnEventId())).thenReturn(Optional.of(mockEvent));
        when(pointEarnStandardRepository.save(any(PointEarnStandard.class))).thenReturn(mockStandard);

        // When
        PointEarnStandardResponse response = pointEarnStandardService.createPointEarnStandard(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("New Standard");
        assertThat(response.point()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(response.status()).isEqualTo(PointEarnStandardStatus.ACTIVATED);
        assertThat(response.pointEarnType()).isEqualTo(PointEarnType.FIXED);
        assertThat(response.eventCode()).isEqualTo(EventCode.SIGNUP);

        verify(pointEarnEventRepository, times(1)).findById(request.pointEarnEventId());
        verify(pointEarnStandardRepository, times(1)).save(any(PointEarnStandard.class));
    }

    @Test
    void testCreatePointEarnStandard_EventNotFound() {
        // Given
        PointEarnStandardRequest request = new PointEarnStandardRequest(
                "Invalid Event",
                BigDecimal.valueOf(50),
                PointEarnType.FIXED,
                PointEarnStandardStatus.ACTIVATED,
                LocalDateTime.now(),
                null,
                99L // Non-existent event ID
        );

        when(pointEarnEventRepository.findById(request.pointEarnEventId())).thenReturn(Optional.empty());

        // When / Then
        assertThrows(PointEarnEventNotFoundException.class, () -> pointEarnStandardService.createPointEarnStandard(request));

        verify(pointEarnEventRepository, times(1)).findById(request.pointEarnEventId());
        verify(pointEarnStandardRepository, never()).save(any(PointEarnStandard.class));
    }

    @Test
    void testGetValidStandardsByEvent() {
        // Given
        EventCode eventCode = EventCode.LOGIN;

        PointEarnEvent mockEvent = new PointEarnEvent(
                1L,
                EventCode.LOGIN,
                new HashSet<>()
        );

        PointEarnStandard mockStandard1 = new PointEarnStandard(
                1L,
                "Login Bonus",
                BigDecimal.valueOf(10),
                PointEarnType.FIXED,
                PointEarnStandardStatus.ACTIVATED,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(5),
                mockEvent
        );

        PointEarnStandard mockStandard2 = new PointEarnStandard(
                2L,
                "Login Promo",
                BigDecimal.valueOf(15),
                PointEarnType.PERCENTAGE,
                PointEarnStandardStatus.ACTIVATED,
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().plusDays(10),
                mockEvent
        );

        when(pointEarnStandardRepository.findValidStandardsByEventCode(eq(eventCode), any(LocalDateTime.class)))
                .thenReturn(List.of(mockStandard1, mockStandard2));

        // When
        List<PointEarnStandardResponse> result = pointEarnStandardService.getValidStandardsByEvent(eventCode);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Login Bonus", result.get(0).name());
        assertEquals(BigDecimal.valueOf(10), result.get(0).point());
        assertEquals(PointEarnType.FIXED, result.get(0).pointEarnType());
        assertEquals(PointEarnStandardStatus.ACTIVATED, result.get(0).status());
        assertEquals(EventCode.LOGIN, result.get(0).eventCode());

        assertEquals("Login Promo", result.get(1).name());
        assertEquals(BigDecimal.valueOf(15), result.get(1).point());
        assertEquals(PointEarnType.PERCENTAGE, result.get(1).pointEarnType());
        assertEquals(PointEarnStandardStatus.ACTIVATED, result.get(1).status());
        assertEquals(EventCode.LOGIN, result.get(1).eventCode());

        verify(pointEarnStandardRepository, times(1))
                .findValidStandardsByEventCode(eq(eventCode), any(LocalDateTime.class));
    }

    @Test
    void testUpdatePointEarnStandard_Success() {
        // Given
        Long standardId = 1L;
        Long eventId = 10L;

        PointEarnEvent mockEvent = new PointEarnEvent(
                eventId,
                EventCode.LOGIN,
                new HashSet<>()
        );

        PointEarnStandard existingStandard = new PointEarnStandard(
                standardId,
                "Old Name",
                BigDecimal.valueOf(5),
                PointEarnType.FIXED,
                PointEarnStandardStatus.ACTIVATED,
                LocalDateTime.now().minusDays(5),
                LocalDateTime.now().plusDays(5),
                mockEvent
        );

        PointEarnStandardRequest updateRequest = new PointEarnStandardRequest(
                "Updated Name",
                BigDecimal.valueOf(10),
                PointEarnType.PERCENTAGE,
                PointEarnStandardStatus.DELETED,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(10),
                eventId
        );

        when(pointEarnStandardRepository.findById(standardId))
                .thenReturn(Optional.of(existingStandard));
        when(pointEarnEventRepository.findById(eventId))
                .thenReturn(Optional.of(mockEvent));

        // When
        PointEarnStandardResponse updatedResponse = pointEarnStandardService.updatePointEarnStandard(standardId, updateRequest);

        // Then
        assertNotNull(updatedResponse);
        assertEquals("Updated Name", updatedResponse.name());
        assertEquals(BigDecimal.valueOf(10), updatedResponse.point());
        assertEquals(PointEarnType.PERCENTAGE, updatedResponse.pointEarnType());
        assertEquals(PointEarnStandardStatus.DELETED, updatedResponse.status());
        assertEquals(EventCode.LOGIN, updatedResponse.eventCode());

        verify(pointEarnStandardRepository, times(1)).findById(standardId);
        verify(pointEarnEventRepository, times(1)).findById(eventId);
    }

    @Test
    void testUpdatePointEarnStandard_StandardNotFound() {
        // Given
        Long standardId = 999L;
        PointEarnStandardRequest request = new PointEarnStandardRequest(
                "Non-existent",
                BigDecimal.valueOf(20),
                PointEarnType.FIXED,
                PointEarnStandardStatus.ACTIVATED,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(5),
                1L
        );

        when(pointEarnStandardRepository.findById(standardId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(PointEarnStandardIdNotFoundException.class, () -> pointEarnStandardService.updatePointEarnStandard(standardId, request));

        verify(pointEarnStandardRepository, times(1)).findById(standardId);
        verify(pointEarnEventRepository, never()).findById(anyLong());
    }

    @Test
    void testUpdatePointEarnStandard_EventNotFound() {
        // Given
        Long standardId = 1L;
        Long eventId = 999L;

        PointEarnStandard existingStandard = new PointEarnStandard(
                standardId,
                "Old Name",
                BigDecimal.valueOf(5),
                PointEarnType.FIXED,
                PointEarnStandardStatus.ACTIVATED,
                LocalDateTime.now().minusDays(5),
                LocalDateTime.now().plusDays(5),
                new PointEarnEvent(10L, EventCode.LOGIN, new HashSet<>())
        );

        PointEarnStandardRequest request = new PointEarnStandardRequest(
                "Name",
                BigDecimal.valueOf(10),
                PointEarnType.PERCENTAGE,
                PointEarnStandardStatus.ACTIVATED,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(5),
                eventId
        );

        when(pointEarnStandardRepository.findById(standardId)).thenReturn(Optional.of(existingStandard));
        when(pointEarnEventRepository.findById(eventId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(PointEarnEventNotFoundException.class, () -> pointEarnStandardService.updatePointEarnStandard(standardId, request));

        verify(pointEarnStandardRepository, times(1)).findById(standardId);
        verify(pointEarnEventRepository, times(1)).findById(eventId);
    }

    @Test
    void testDeletePointEarnStandard_Success() {
        // Given
        Long standardId = 2L;
        PointEarnEvent mockEvent = new PointEarnEvent(
                1L,
                EventCode.ORDER,
                new HashSet<>()
        );

        PointEarnStandard existingStandard = new PointEarnStandard(
                standardId,
                "To Delete",
                BigDecimal.valueOf(50),
                PointEarnType.FIXED,
                PointEarnStandardStatus.ACTIVATED,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(10),
                mockEvent
        );

        when(pointEarnStandardRepository.findById(standardId)).thenReturn(Optional.of(existingStandard));

        // When
        pointEarnStandardService.deletePointEarnStandard(standardId);

        // Then
        assertEquals(PointEarnStandardStatus.DELETED, existingStandard.getStatus());

        verify(pointEarnStandardRepository, times(1)).findById(standardId);
    }

    @Test
    void testDeletePointEarnStandard_StandardNotFound() {
        // Given
        Long standardId = 999L;
        when(pointEarnStandardRepository.findById(standardId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(PointEarnStandardIdNotFoundException.class, () -> pointEarnStandardService.deletePointEarnStandard(standardId));

        verify(pointEarnStandardRepository, times(1)).findById(standardId);
    }

}
