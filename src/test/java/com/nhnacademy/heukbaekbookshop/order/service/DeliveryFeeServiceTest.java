package com.nhnacademy.heukbaekbookshop.order.service;

import com.nhnacademy.heukbaekbookshop.order.domain.DeliveryFee;
import com.nhnacademy.heukbaekbookshop.order.dto.request.DeliveryFeeCreateRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.request.DeliveryFeeUpdateRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.response.DeliveryFeeCreateResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.DeliveryFeeDeleteResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.DeliveryFeeDetailResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.DeliveryFeeUpdateResponse;
import com.nhnacademy.heukbaekbookshop.order.exception.DeliveryFeeAlreadyExistsException;
import com.nhnacademy.heukbaekbookshop.order.exception.DeliveryNotFoundException;
import com.nhnacademy.heukbaekbookshop.order.repository.DeliveryFeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryFeeServiceTest {

    @InjectMocks
    private DeliveryFeeService deliveryFeeService;

    @Mock
    private DeliveryFeeRepository deliveryFeeRepository;

    @Test
    void createDeliveryFee_Success() {
        // Given
        DeliveryFeeCreateRequest request = new DeliveryFeeCreateRequest("Express Delivery", BigDecimal.valueOf(5000), BigDecimal.valueOf(30000));

        when(deliveryFeeRepository.findByName(request.name())).thenReturn(Optional.empty());

        // When
        DeliveryFeeCreateResponse response = deliveryFeeService.createDeliveryFee(request);

        // Then
        assertNotNull(response);
        assertEquals(request.name(), response.name());
        assertEquals(request.fee(), response.fee());
        assertEquals(request.minimumOrderAmount(), response.minimumOrderAmount());
        verify(deliveryFeeRepository, times(1)).save(any(DeliveryFee.class));
    }

    @Test
    void createDeliveryFee_Failure_DuplicateName() {
        // Given
        DeliveryFeeCreateRequest request = new DeliveryFeeCreateRequest("Express Delivery", BigDecimal.valueOf(5000), BigDecimal.valueOf(30000));

        when(deliveryFeeRepository.findByName(request.name())).thenReturn(Optional.of(new DeliveryFee()));

        // When & Then
        assertThrows(DeliveryFeeAlreadyExistsException.class, () -> deliveryFeeService.createDeliveryFee(request));
        verify(deliveryFeeRepository, never()).save(any(DeliveryFee.class));
    }

    @Test
    void getDeliveryFee_Success() {
        // Given
        Long deliveryFeeId = 1L;
        DeliveryFee deliveryFee = new DeliveryFee(1L, "Express Delivery", BigDecimal.valueOf(5000), BigDecimal.valueOf(30000));

        when(deliveryFeeRepository.findById(deliveryFeeId)).thenReturn(Optional.of(deliveryFee));

        // When
        DeliveryFeeDetailResponse response = deliveryFeeService.getDeliveryFee(deliveryFeeId);

        // Then
        assertNotNull(response);
        assertEquals(deliveryFee.getName(), response.name());
        assertEquals(deliveryFee.getFee(), response.fee());
        assertEquals(deliveryFee.getMinimumOrderAmount(), response.minimumOrderAmount());
    }

    @Test
    void getDeliveryFee_Failure_NotFound() {
        // Given
        Long deliveryFeeId = 1L;

        when(deliveryFeeRepository.findById(deliveryFeeId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(DeliveryNotFoundException.class, () -> deliveryFeeService.getDeliveryFee(deliveryFeeId));
    }

    @Test
    void updateDeliveryFee_Success() {
        // Given
        Long deliveryFeeId = 1L;
        DeliveryFee deliveryFee = new DeliveryFee(1L, "Express Delivery", BigDecimal.valueOf(5000), BigDecimal.valueOf(30000));
        DeliveryFeeUpdateRequest request = new DeliveryFeeUpdateRequest("Super Express", BigDecimal.valueOf(8000), BigDecimal.valueOf(40000));

        when(deliveryFeeRepository.findById(deliveryFeeId)).thenReturn(Optional.of(deliveryFee));

        // When
        DeliveryFeeUpdateResponse response = deliveryFeeService.updateDeliveryFee(deliveryFeeId, request);

        // Then
        assertNotNull(response);
        assertEquals(request.name(), response.name());
        assertEquals(request.fee(), response.fee());
        assertEquals(request.minimumOrderAmount(), response.minimumOrderAmount());
        verify(deliveryFeeRepository, times(1)).save(deliveryFee);
    }

    @Test
    void deleteDeliveryFee_Success() {
        // Given
        Long deliveryFeeId = 1L;
        DeliveryFee deliveryFee = new DeliveryFee(1L, "Express Delivery", BigDecimal.valueOf(5000), BigDecimal.valueOf(30000));

        when(deliveryFeeRepository.findById(deliveryFeeId)).thenReturn(Optional.of(deliveryFee));

        // When
        DeliveryFeeDeleteResponse response = deliveryFeeService.deleteDeliveryFee(deliveryFeeId);

        // Then
        assertNotNull(response);
        assertEquals(deliveryFee.getName(), response.name());
        verify(deliveryFeeRepository, times(1)).delete(deliveryFee);
    }

    @Test
    void getDeliveryFees_Success() {
        // Given
        Page<DeliveryFee> page = new PageImpl<>(List.of(new DeliveryFee(1L, "Express Delivery", BigDecimal.valueOf(5000), BigDecimal.valueOf(30000))));
        when(deliveryFeeRepository.findAll(any(Pageable.class))).thenReturn(page);

        // When
        Page<DeliveryFeeDetailResponse> response = deliveryFeeService.getDeliveryFees(PageRequest.of(0, 10));

        // Then
        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals("Express Delivery", response.getContent().get(0).name());
    }

    @Test
    void getDeliveryFeeByMinimumOrderAmount_Success() {
        // Given
        BigDecimal minimumOrderAmount = BigDecimal.valueOf(30000);
        DeliveryFee deliveryFee = new DeliveryFee(1L, "Express Delivery", BigDecimal.valueOf(5000), minimumOrderAmount);

        when(deliveryFeeRepository.findByMinimumOrderAmount(minimumOrderAmount)).thenReturn(Optional.of(deliveryFee));

        // When
        BigDecimal fee = deliveryFeeService.getDeliveryFeeByMinimumOrderAmount(minimumOrderAmount);

        // Then
        assertNotNull(fee);
        assertEquals(deliveryFee.getFee(), fee);
    }
}