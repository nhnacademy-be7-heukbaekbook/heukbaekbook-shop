package com.nhnacademy.heukbaekbookshop.point.service.impl;

import com.nhnacademy.heukbaekbookshop.point.domain.PointEarnStandard;
import com.nhnacademy.heukbaekbookshop.point.domain.PointEarnStandardStatus;
import com.nhnacademy.heukbaekbookshop.point.dto.mapper.PointEarnStandardMapper;
import com.nhnacademy.heukbaekbookshop.point.dto.request.PointEarnStandardRequest;
import com.nhnacademy.heukbaekbookshop.point.dto.response.PointEarnStandardResponse;
import com.nhnacademy.heukbaekbookshop.point.exception.PointEarnStandardIdNotFoundException;
import com.nhnacademy.heukbaekbookshop.point.repository.PointEarnStandardRepository;
import com.nhnacademy.heukbaekbookshop.point.service.PointEarnStandardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.nhnacademy.heukbaekbookshop.point.dto.mapper.PointEarnStandardMapper.toEntity;
import static com.nhnacademy.heukbaekbookshop.point.dto.mapper.PointEarnStandardMapper.toResponse;

@Service
@RequiredArgsConstructor
public class PointEarnStandardServiceImpl implements PointEarnStandardService {

    private final PointEarnStandardRepository pointEarnStandardRepository;

    @Override
    public PointEarnStandardResponse createPointEarnStandard(PointEarnStandardRequest pointEarnStandardRequest) {
        PointEarnStandard pointEarnStandard = toEntity(pointEarnStandardRequest);
        PointEarnStandard savedPointEarnStandard = pointEarnStandardRepository.save(pointEarnStandard);
        return toResponse(savedPointEarnStandard);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PointEarnStandardResponse> getAllPointEarnStandard() {
        return pointEarnStandardRepository.findAllByStatusNot(PointEarnStandardStatus.DELETED).stream()
                .map(PointEarnStandardMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public PointEarnStandardResponse updatePointEarnStandard(Long id, PointEarnStandardRequest pointEarnStandardRequest) {
        PointEarnStandard pointEarnStandard = pointEarnStandardRepository
                .findById(id)
                .orElseThrow(() -> new PointEarnStandardIdNotFoundException(String.valueOf(id)));

        pointEarnStandard.setName(pointEarnStandardRequest.name());
        pointEarnStandard.setPoint(pointEarnStandardRequest.point());
        pointEarnStandard.setStatus(pointEarnStandardRequest.status());
        pointEarnStandard.setTriggerEvent(pointEarnStandardRequest.triggerEvent());

        return toResponse(pointEarnStandard);
    }

    @Override
    @Transactional
    public void deletePointEarnStandard(Long id) {
        PointEarnStandard pointEarnStandard = pointEarnStandardRepository
                .findById(id)
                .orElseThrow(() -> new PointEarnStandardIdNotFoundException(String.valueOf(id)));

        pointEarnStandard.setStatus(PointEarnStandardStatus.DELETED);
    }
}
