package com.nhnacademy.heukbaekbookshop.point.earn.dto.mapper;

import com.nhnacademy.heukbaekbookshop.point.earn.domain.PointEarnEvent;
import com.nhnacademy.heukbaekbookshop.point.earn.domain.PointEarnStandard;
import com.nhnacademy.heukbaekbookshop.point.earn.dto.request.PointEarnStandardRequest;
import com.nhnacademy.heukbaekbookshop.point.earn.dto.response.PointEarnStandardResponse;

public class PointEarnStandardMapper {

    private PointEarnStandardMapper() {
    }

    public static PointEarnStandard toEntity(PointEarnStandardRequest request, PointEarnEvent event) {
        PointEarnStandard pointEarnStandard = new PointEarnStandard();
        pointEarnStandard.setName(request.name());
        pointEarnStandard.setPoint(request.point());
        pointEarnStandard.setStatus(request.status());
        pointEarnStandard.setPointEarnType(request.pointEarnType());
        pointEarnStandard.setPointEarnStart(request.pointEarnStart());
        pointEarnStandard.setPointEarnEnd(request.pointEarnEnd());
        pointEarnStandard.setPointEarnEvent(event);
        return pointEarnStandard;
    }

    public static PointEarnStandardResponse toResponse(PointEarnStandard pointEarnStandard) {
        return new PointEarnStandardResponse(
                pointEarnStandard.getId(),
                pointEarnStandard.getName(),
                pointEarnStandard.getPoint(),
                pointEarnStandard.getStatus(),
                pointEarnStandard.getPointEarnType(),
                pointEarnStandard.getPointEarnStart(),
                pointEarnStandard.getPointEarnEnd(),
                pointEarnStandard.getPointEarnEvent().getEventCode()
        );
    }
}
