package com.nhnacademy.heukbaekbookshop.order.service;

import com.nhnacademy.heukbaekbookshop.order.domain.DeliveryFee;
import com.nhnacademy.heukbaekbookshop.order.dto.request.DeliveryFeeCreateRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.request.DeliveryFeeUpdateRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.response.DeliveryFeeCreateResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.DeliveryFeeDeleteResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.DeliveryFeeDetailResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.DeliveryFeeUpdateResponse;
import com.nhnacademy.heukbaekbookshop.order.exception.DeliveryFeeAlreadyExistsException;
import com.nhnacademy.heukbaekbookshop.order.exception.DeliveryFeeNotFoundException;
import com.nhnacademy.heukbaekbookshop.order.exception.DeliveryNotFoundException;
import com.nhnacademy.heukbaekbookshop.order.repository.DeliveryFeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DeliveryFeeService {

    private final DeliveryFeeRepository deliveryFeeRepository;

    public DeliveryFeeService(DeliveryFeeRepository deliveryFeeRepository) {
        this.deliveryFeeRepository = deliveryFeeRepository;
    }

    public DeliveryFeeCreateResponse createDeliveryFee(DeliveryFeeCreateRequest request) {
        if (deliveryFeeRepository.findByName(request.name()).isPresent()) {
            throw new DeliveryFeeAlreadyExistsException("이미 존재하는 배송비 이름입니다.");
        }

        DeliveryFee deliveryFee = DeliveryFee.builder()
                .name(request.name())
                .fee(request.fee())
                .minimumOrderAmount(request.minimumOrderAmount())
                .build();

        deliveryFeeRepository.save(deliveryFee);

        return new DeliveryFeeCreateResponse(request.name(), request.fee(), request.minimumOrderAmount());
    }

    public DeliveryFeeDetailResponse getDeliveryFee(Long deliveryFeeId) {
        DeliveryFee deliveryFee = deliveryFeeRepository.findById(deliveryFeeId)
                .orElseThrow(() -> new DeliveryNotFoundException("존재하지 않는 배송비 정보입니다."));

        return new DeliveryFeeDetailResponse(deliveryFee.getId(), deliveryFee.getName(), deliveryFee.getFee(), deliveryFee.getMinimumOrderAmount());
    }

    public DeliveryFeeUpdateResponse updateDeliveryFee(Long deliveryFeeId, DeliveryFeeUpdateRequest request) {
        DeliveryFee deliveryFee = deliveryFeeRepository.findById(deliveryFeeId)
                .orElseThrow(() -> new DeliveryNotFoundException("존재하지 않는 배송비 정보입니다."));

        deliveryFee.setName(request.name());
        deliveryFee.setFee(request.fee());
        deliveryFee.setMinimumOrderAmount(request.minimumOrderAmount());

        deliveryFeeRepository.save(deliveryFee);

        return new DeliveryFeeUpdateResponse(request.name(), request.fee(), request.minimumOrderAmount());
    }

    public DeliveryFeeDeleteResponse deleteDeliveryFee(Long deliveryFeeId) {
        DeliveryFee deliveryFee = deliveryFeeRepository.findById(deliveryFeeId)
                .orElseThrow(() -> new DeliveryNotFoundException("존재하지 않는 배송비 정보입니다."));

        deliveryFeeRepository.delete(deliveryFee);

        return new DeliveryFeeDeleteResponse(deliveryFee.getId(), deliveryFee.getName(), deliveryFee.getFee(), deliveryFee.getMinimumOrderAmount());
    }

    public Page<DeliveryFeeDetailResponse> getDeliveryFees(Pageable pageable) {
        Page<DeliveryFee> deliveryFees = deliveryFeeRepository.findAll(pageable);
        return deliveryFees.map(deliveryFee -> new DeliveryFeeDetailResponse(deliveryFee.getId(), deliveryFee.getName(), deliveryFee.getFee(), deliveryFee.getMinimumOrderAmount()));
    }

    public BigDecimal getDeliveryFeeByMinimumOrderAmount(BigDecimal minimumOrderAmount) {
        return deliveryFeeRepository.findByMinimumOrderAmount(minimumOrderAmount)
                .orElseThrow(() -> new DeliveryFeeNotFoundException("delivery fee not found")).getFee();
    }
}
