package com.nhnacademy.heukbaekbookshop.memberset.grade.service;

import com.nhnacademy.heukbaekbookshop.memberset.address.domain.MemberAddress;
import com.nhnacademy.heukbaekbookshop.memberset.address.exception.MemberAddressNotFoundException;
import com.nhnacademy.heukbaekbookshop.memberset.grade.domain.Grade;
import com.nhnacademy.heukbaekbookshop.memberset.grade.dto.GradeDto;
import com.nhnacademy.heukbaekbookshop.memberset.grade.exception.GradeAlreadyExistsException;
import com.nhnacademy.heukbaekbookshop.memberset.grade.exception.GradeNotFoundException;
import com.nhnacademy.heukbaekbookshop.memberset.grade.repository.GradeRepository;
import com.nhnacademy.heukbaekbookshop.memberset.grade.service.impl.GradeServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GradeServiceTest {
    @Mock
    private GradeRepository gradeRepository;
    @InjectMocks
    private GradeServiceImpl gradeService;

    private final Long testGradeId = 1L;
    private final String testGradeName = "testGradeName";
    private final BigDecimal testPointPercentage = BigDecimal.valueOf(1.1);
    private final BigDecimal testPromotionStandard = BigDecimal.valueOf(10000L);

    @Test
    @DisplayName("등급 생성 시 중복 발생")
    void createGrade_GradeAlreadyExists_ExceptionThrown() {
        // given
        when(gradeRepository.existsByGradeName(any())).thenReturn(true);

        // when & then
        assertThrows(GradeAlreadyExistsException.class, () -> gradeService.createGrade(mock(GradeDto.class)));
        verify(gradeRepository, times(1)).existsByGradeName(any());
    }

    @Test
    @DisplayName("등급 생성 성공")
    void createGrade_CreateGrade_Success() {
        // given
        Grade testGrade = Grade.builder()
                .gradeName(testGradeName)
                .pointPercentage(testPointPercentage)
                .promotionStandard(testPromotionStandard)
                .build();
        GradeDto testGradeDto = new GradeDto(testGradeName, testPointPercentage, testPromotionStandard);
        when(gradeRepository.existsByGradeName(any())).thenReturn(false);
        when(gradeRepository.save(any(Grade.class))).thenReturn(testGrade);

        // when
        GradeDto resultGradeDto = gradeService.createGrade(testGradeDto);

        // then
        verify(gradeRepository, times(1)).save(any(Grade.class));
        assertNotNull(resultGradeDto);
    }

    @Test
    @DisplayName("등급 조회 시 조회 실패")
    void getGrade_GradeNotFound_ExceptionThrown() {
        // given
        when(gradeRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(GradeNotFoundException.class, () -> gradeService.getGrade(testGradeId));
        verify(gradeRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("등급 리스트 조회 성공")
    void getAllGradeList() {
        // given
        List<Grade> mockedGradeList = List.of(
                mock(Grade.class), mock(Grade.class));
        when(gradeRepository.findAll()).thenReturn(mockedGradeList);

        // when
        List<GradeDto> result = gradeService.getAllGradeList();

        // then
        assertThat(result).hasSize(2);
        verify(gradeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("등급 수정 시 등급 조회 실패")
    void updateGrade_GradeNotFound_ExceptionThrown() {
        // given
        GradeDto testGradeDto = new GradeDto(testGradeName, testPointPercentage, testPromotionStandard);
        when(gradeRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(GradeNotFoundException.class, () -> gradeService.updateGrade(testGradeId, testGradeDto));
        verify(gradeRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("등급 수정 시 등급 조회 실패")
    void updateGrade_AlreadyExists_ExceptionThrown() {
        // given
        GradeDto testGradeDto = new GradeDto(testGradeName, testPointPercentage, testPromotionStandard);
        Grade testGrade = Grade.builder()
                .gradeName(testGradeName)
                .pointPercentage(testPointPercentage)
                .promotionStandard(testPromotionStandard)
                .build();

        when(gradeRepository.findById(any())).thenReturn(Optional.of(testGrade));
        when(gradeRepository.existsByGradeName(any())).thenReturn(true);

        // when & then
        assertThrows(GradeAlreadyExistsException.class, () -> gradeService.updateGrade(testGradeId, testGradeDto));
        verify(gradeRepository, times(1)).existsByGradeName(any());
    }

    @Test
    @DisplayName("등급 수정 성공")
    void updateGrade_UpdateGrade_Success() {
        // given
        GradeDto testGradeDto = new GradeDto("newGradeName", testPointPercentage, testPromotionStandard);
        Grade testGrade = Grade.builder()
                .gradeName(testGradeName)
                .pointPercentage(testPointPercentage)
                .promotionStandard(testPromotionStandard)
                .build();

        when(gradeRepository.findById(any())).thenReturn(Optional.of(testGrade));
        when(gradeRepository.existsByGradeName(any())).thenReturn(false);

        // when
        GradeDto result = gradeService.updateGrade(testGradeId, testGradeDto);

        // then
        verify(gradeRepository, times(1)).findById(any());
        verify(gradeRepository, times(1)).existsByGradeName(any());
        assertEquals("newGradeName", result.gradeName());
    }

    @Test
    @DisplayName("등급 삭제 시 조회 실패")
    void deleteGrade_GradeNotFound_ExceptionThrown() {
        // given
        when(gradeRepository.existsById(any())).thenReturn(false);

        // when & then
        assertThrows(GradeNotFoundException.class, () -> gradeService.deleteGrade(testGradeId));
        verify(gradeRepository, times(1)).existsById(any());
    }

    @Test
    @DisplayName("등급 삭제 성공")
    void deleteMemberAddress_DeleteMemberAddress_Success() {
        // given
        when(gradeRepository.existsById(any())).thenReturn(true);
        // when
        gradeService.deleteGrade(testGradeId);
        // then
        verify(gradeRepository, times(1)).deleteById(testGradeId);
    }
}
