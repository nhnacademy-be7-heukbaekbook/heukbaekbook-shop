package com.nhnacademy.heukbaekbookshop.point.dto.mapper;

import com.nhnacademy.heukbaekbookshop.point.domain.earn.PointEarnStandard;
import com.nhnacademy.heukbaekbookshop.point.dto.request.PointEarnStandardRequest;
import com.nhnacademy.heukbaekbookshop.point.dto.response.PointEarnStandardResponse;

public class PointEarnStandardMapper {

    private PointEarnStandardMapper() {
    }

    public static PointEarnStandard toEntity(PointEarnStandardRequest request) {
        PointEarnStandard pointEarnStandard = new PointEarnStandard();
        pointEarnStandard.setName(request.name());
        pointEarnStandard.setPoint(request.point());
        pointEarnStandard.setStatus(request.status());
        pointEarnStandard.setTriggerEvent(request.triggerEvent());
        return pointEarnStandard;
    }

    public static PointEarnStandardResponse toResponse(PointEarnStandard pointEarnStandard) {
        return new PointEarnStandardResponse(
                pointEarnStandard.getId(),
                pointEarnStandard.getName(),
                pointEarnStandard.getPoint(),
                pointEarnStandard.getStatus(),
                pointEarnStandard.getTriggerEvent()
        );
    }
}
