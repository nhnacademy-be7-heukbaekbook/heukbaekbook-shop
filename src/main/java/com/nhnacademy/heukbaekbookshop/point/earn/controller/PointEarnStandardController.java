package com.nhnacademy.heukbaekbookshop.point.earn.controller;

import com.nhnacademy.heukbaekbookshop.point.earn.domain.EventCode;
import com.nhnacademy.heukbaekbookshop.point.earn.dto.request.PointEarnStandardRequest;
import com.nhnacademy.heukbaekbookshop.point.earn.dto.response.PointEarnStandardResponse;
import com.nhnacademy.heukbaekbookshop.point.earn.service.PointEarnStandardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/points/earn-standards")
public class PointEarnStandardController {

    private final PointEarnStandardService pointEarnStandardService;

    @PostMapping
    public ResponseEntity<PointEarnStandardResponse> createPointEarnStandard(
            @Valid @RequestBody PointEarnStandardRequest request) {

        PointEarnStandardResponse createdResponse = pointEarnStandardService.createPointEarnStandard(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdResponse);
    }

    @GetMapping("/event/{eventCode}")
    public ResponseEntity<List<PointEarnStandardResponse>> getValidStandardsByEvent(@PathVariable String eventCode) {
        List<PointEarnStandardResponse> pointEarnStandardResponses = pointEarnStandardService.getValidStandardsByEvent(
                EventCode.valueOf(eventCode.toUpperCase())
        );

        return ResponseEntity.ok(pointEarnStandardResponses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PointEarnStandardResponse> updatePointEarnStandard(
            @PathVariable Long id,
            @Valid @RequestBody PointEarnStandardRequest pointEarnStandardRequest) {

        PointEarnStandardResponse updatedResponse = pointEarnStandardService.updatePointEarnStandard(id, pointEarnStandardRequest);

        return ResponseEntity.ok(updatedResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePointEarnStandard(@PathVariable Long id) {
        pointEarnStandardService.deletePointEarnStandard(id);

        return ResponseEntity.noContent().build();
    }
}
