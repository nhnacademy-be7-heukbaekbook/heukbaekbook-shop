package com.nhnacademy.heukbaekbookshop.memberset.grade.service.impl;

import com.nhnacademy.heukbaekbookshop.memberset.grade.domain.Grade;
import com.nhnacademy.heukbaekbookshop.memberset.grade.dto.GradeDto;
import com.nhnacademy.heukbaekbookshop.memberset.grade.dto.mapper.GradeMapper;
import com.nhnacademy.heukbaekbookshop.memberset.grade.exception.GradeAlreadyExistsException;
import com.nhnacademy.heukbaekbookshop.memberset.grade.exception.GradeNotFoundException;
import com.nhnacademy.heukbaekbookshop.memberset.grade.repository.GradeRepository;
import com.nhnacademy.heukbaekbookshop.memberset.grade.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {
    private final GradeRepository gradeRepository;

    @Override
    public GradeDto createGrade(GradeDto gradeDto) {
        if (gradeRepository.existsByGradeName(gradeDto.gradeName())) {
            throw new GradeAlreadyExistsException();
        }
        return GradeMapper.createGradeResponse(
                gradeRepository.save(GradeMapper.createGradeEntity(gradeDto)));
    }

    @Override
    public GradeDto getGrade(Long gradeId) {
        return GradeMapper.createGradeResponse(
                gradeRepository.findById(gradeId)
                        .orElseThrow(GradeNotFoundException::new));
    }

    @Override
    public List<GradeDto> getAllGradeList() {
        return GradeMapper.createGradeResponseList(gradeRepository.findAll());
    }

    @Override
    public GradeDto updateGrade(Long gradeId, GradeDto gradeDto) {
        Grade grade = gradeRepository.findById(gradeId).orElseThrow(GradeNotFoundException::new);
        if (gradeRepository.existsByGradeName(gradeDto.gradeName())) {
            throw new GradeAlreadyExistsException();
        }
        return GradeMapper.createGradeResponse(grade.modifyGrade(gradeDto));
    }

    @Override
    public void deleteGrade(Long gradeId) {
        if (!gradeRepository.existsById(gradeId)) {
            throw new GradeNotFoundException();
        }
        gradeRepository.deleteById(gradeId);
    }
}
