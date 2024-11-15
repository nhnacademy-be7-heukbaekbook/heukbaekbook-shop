package com.nhnacademy.heukbaekbookshop.point.earn.service.impl;

import com.nhnacademy.heukbaekbookshop.point.earn.domain.EventCode;
import com.nhnacademy.heukbaekbookshop.point.earn.domain.PointEarnEvent;
import com.nhnacademy.heukbaekbookshop.point.earn.domain.PointEarnStandard;
import com.nhnacademy.heukbaekbookshop.point.earn.domain.PointEarnStandardStatus;
import com.nhnacademy.heukbaekbookshop.point.earn.dto.mapper.PointEarnStandardMapper;
import com.nhnacademy.heukbaekbookshop.point.earn.dto.request.PointEarnStandardRequest;
import com.nhnacademy.heukbaekbookshop.point.earn.dto.response.PointEarnStandardResponse;
import com.nhnacademy.heukbaekbookshop.point.earn.exception.PointEarnEventNotFoundException;
import com.nhnacademy.heukbaekbookshop.point.earn.exception.PointEarnStandardIdNotFoundException;
import com.nhnacademy.heukbaekbookshop.point.earn.repository.PointEarnEventRepository;
import com.nhnacademy.heukbaekbookshop.point.earn.repository.PointEarnStandardRepository;
import com.nhnacademy.heukbaekbookshop.point.earn.service.PointEarnStandardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.nhnacademy.heukbaekbookshop.point.earn.dto.mapper.PointEarnStandardMapper.toEntity;
import static com.nhnacademy.heukbaekbookshop.point.earn.dto.mapper.PointEarnStandardMapper.toResponse;

@Service
@RequiredArgsConstructor
public class PointEarnStandardServiceImpl implements PointEarnStandardService {

    private final PointEarnStandardRepository pointEarnStandardRepository;
    private final PointEarnEventRepository pointEarnEventRepository;

    @Override
    @Transactional
    public PointEarnStandardResponse createPointEarnStandard(PointEarnStandardRequest pointEarnStandardRequest) {
        PointEarnEvent earnEvent = pointEarnEventRepository.findById(pointEarnStandardRequest.pointEarnEventId())
                .orElseThrow(() -> new PointEarnEventNotFoundException(String.valueOf(pointEarnStandardRequest.pointEarnEventId())));

        PointEarnStandard pointEarnStandard = toEntity(pointEarnStandardRequest, earnEvent);

        PointEarnStandard savedPointEarnStandard = pointEarnStandardRepository.save(pointEarnStandard);

        return toResponse(savedPointEarnStandard);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PointEarnStandardResponse> getValidStandardsByEvent(EventCode eventCode) {
        return pointEarnStandardRepository.findValidStandardsByEventCode(eventCode, LocalDateTime.now())
                .stream()
                .map(PointEarnStandardMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public PointEarnStandardResponse updatePointEarnStandard(Long id, PointEarnStandardRequest pointEarnStandardRequest) {
        PointEarnStandard pointEarnStandard = pointEarnStandardRepository.findById(id)
                .orElseThrow(() -> new PointEarnStandardIdNotFoundException(String.valueOf(id)));

        PointEarnEvent earnEvent = pointEarnEventRepository.findById(pointEarnStandardRequest.pointEarnEventId())
                .orElseThrow(() -> new PointEarnEventNotFoundException(String.valueOf(pointEarnStandardRequest.pointEarnEventId())));

        pointEarnStandard.setName(pointEarnStandardRequest.name());
        pointEarnStandard.setPoint(pointEarnStandardRequest.point());
        pointEarnStandard.setPointEarnType(pointEarnStandardRequest.pointEarnType());
        pointEarnStandard.setStatus(pointEarnStandardRequest.status());
        pointEarnStandard.setPointEarnStart(pointEarnStandardRequest.pointEarnStart());
        pointEarnStandard.setPointEarnEnd(pointEarnStandardRequest.pointEarnEnd());
        pointEarnStandard.setPointEarnEvent(earnEvent);

        return toResponse(pointEarnStandard);
    }

    @Override
    @Transactional
    public void deletePointEarnStandard(Long id) {
        PointEarnStandard pointEarnStandard = pointEarnStandardRepository.findById(id)
                .orElseThrow(() -> new PointEarnStandardIdNotFoundException(String.valueOf(id)));

        pointEarnStandard.setStatus(PointEarnStandardStatus.DELETED);
    }
}
