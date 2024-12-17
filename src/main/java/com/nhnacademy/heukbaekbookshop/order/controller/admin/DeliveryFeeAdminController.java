package com.nhnacademy.heukbaekbookshop.order.controller.admin;

import com.nhnacademy.heukbaekbookshop.order.dto.request.DeliveryFeeCreateRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.request.DeliveryFeeUpdateRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.response.DeliveryFeeCreateResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.DeliveryFeeDeleteResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.DeliveryFeeDetailResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.DeliveryFeeUpdateResponse;
import com.nhnacademy.heukbaekbookshop.order.service.DeliveryFeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/delivery-fee")
public class DeliveryFeeAdminController {

    private final DeliveryFeeService deliveryFeeService;

    public DeliveryFeeAdminController(DeliveryFeeService deliveryFeeService) {
        this.deliveryFeeService = deliveryFeeService;
    }

    @PostMapping
    public ResponseEntity<DeliveryFeeCreateResponse> createDeliveryFee(@RequestBody DeliveryFeeCreateRequest request) {
        DeliveryFeeCreateResponse response = deliveryFeeService.createDeliveryFee(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{delivery-fee-id}")
    public ResponseEntity<DeliveryFeeDetailResponse> getDeliveryFee(@PathVariable("delivery-fee-id") Long deliveryFeeId) {
        DeliveryFeeDetailResponse response = deliveryFeeService.getDeliveryFee(deliveryFeeId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{delivery-fee-id}")
    public ResponseEntity<DeliveryFeeUpdateResponse> updateDeliveryFee(@PathVariable("delivery-fee-id") Long deliveryFeeId, @RequestBody DeliveryFeeUpdateRequest request) {
        DeliveryFeeUpdateResponse response = deliveryFeeService.updateDeliveryFee(deliveryFeeId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{delivery-fee-id}")
    public ResponseEntity<DeliveryFeeDeleteResponse> deleteDeliveryFee(@PathVariable("delivery-fee-id") Long deliveryFeeId) {
        DeliveryFeeDeleteResponse response = deliveryFeeService.deleteDeliveryFee(deliveryFeeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<DeliveryFeeDetailResponse>> getDeliveryFees(Pageable pageable) {
        Page<DeliveryFeeDetailResponse> responses = deliveryFeeService.getDeliveryFees(pageable);
        return ResponseEntity.ok(responses);
    }
}
