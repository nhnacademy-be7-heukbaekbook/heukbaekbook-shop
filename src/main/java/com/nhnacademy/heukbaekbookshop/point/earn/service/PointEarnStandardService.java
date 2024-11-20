package com.nhnacademy.heukbaekbookshop.point.earn.service;

import com.nhnacademy.heukbaekbookshop.point.earn.domain.EventCode;
import com.nhnacademy.heukbaekbookshop.point.earn.dto.request.PointEarnStandardRequest;
import com.nhnacademy.heukbaekbookshop.point.earn.dto.response.PointEarnStandardResponse;

import java.util.List;

public interface PointEarnStandardService {
    PointEarnStandardResponse createPointEarnStandard(PointEarnStandardRequest pointEarnStandardRequest);

    List<PointEarnStandardResponse> getValidStandardsByEvent(EventCode eventCode);

    PointEarnStandardResponse updatePointEarnStandard(Long id, PointEarnStandardRequest request);

    void deletePointEarnStandard(Long id);
}
