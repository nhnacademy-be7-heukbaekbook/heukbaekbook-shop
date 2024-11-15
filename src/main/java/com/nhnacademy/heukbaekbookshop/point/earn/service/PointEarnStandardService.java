package com.nhnacademy.heukbaekbookshop.point.service;

import com.nhnacademy.heukbaekbookshop.point.dto.request.PointEarnStandardRequest;
import com.nhnacademy.heukbaekbookshop.point.dto.response.PointEarnStandardResponse;

import java.util.List;

public interface PointEarnStandardService {
    PointEarnStandardResponse createPointEarnStandard(PointEarnStandardRequest pointEarnStandardRequest);

    List<PointEarnStandardResponse> getAllPointEarnStandard();

    PointEarnStandardResponse updatePointEarnStandard(Long id, PointEarnStandardRequest request);

    void deletePointEarnStandard(Long id);
}
