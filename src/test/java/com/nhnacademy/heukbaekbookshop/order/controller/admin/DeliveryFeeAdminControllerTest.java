package com.nhnacademy.heukbaekbookshop.order.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.heukbaekbookshop.order.dto.request.DeliveryFeeCreateRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.request.DeliveryFeeUpdateRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.response.DeliveryFeeCreateResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.DeliveryFeeDeleteResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.DeliveryFeeDetailResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.DeliveryFeeUpdateResponse;
import com.nhnacademy.heukbaekbookshop.order.service.DeliveryFeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeliveryFeeAdminController.class)
class DeliveryFeeAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeliveryFeeService deliveryFeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateDeliveryFee() throws Exception {
        // 유효한 DeliveryFeeCreateRequest 데이터 생성
        DeliveryFeeCreateRequest validRequest = new DeliveryFeeCreateRequest(
                "Standard Delivery",
                new BigDecimal("5000.0"),
                new BigDecimal("30000.0")
        );

        // Mock DeliveryFeeCreateResponse 생성
        DeliveryFeeCreateResponse mockResponse = new DeliveryFeeCreateResponse(
                "Standard Delivery",
                new BigDecimal("5000.0"),
                new BigDecimal("30000.0")
        );

        // Mock 서비스 동작 설정
        when(deliveryFeeService.createDeliveryFee(any(DeliveryFeeCreateRequest.class))).thenReturn(mockResponse);

        // 테스트 수행
        mockMvc.perform(post("/api/admin/delivery-fee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Standard Delivery"))
                .andExpect(jsonPath("$.fee").value("5000.0"))
                .andExpect(jsonPath("$.minimumOrderAmount").value("30000.0"));

        // Verify 서비스 호출 확인
        verify(deliveryFeeService, times(1)).createDeliveryFee(any(DeliveryFeeCreateRequest.class));
    }

    @Test
    void testGetDeliveryFee() throws Exception {
        // Mock DeliveryFeeDetailResponse 생성
        DeliveryFeeDetailResponse mockResponse = new DeliveryFeeDetailResponse(
                1L,
                "Standard Delivery",
                new BigDecimal("5000.0"),
                new BigDecimal("30000.0")
        );

        // Mock 서비스 동작 설정
        when(deliveryFeeService.getDeliveryFee(1L)).thenReturn(mockResponse);

        // 테스트 수행
        mockMvc.perform(get("/api/admin/delivery-fee/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Standard Delivery"))
                .andExpect(jsonPath("$.fee").value("5000.0"))
                .andExpect(jsonPath("$.minimumOrderAmount").value("30000.0"));
    }

    @Test
    void testUpdateDeliveryFee() throws Exception {
        // 유효한 DeliveryFeeUpdateRequest 데이터 생성
        DeliveryFeeUpdateRequest validRequest = new DeliveryFeeUpdateRequest(
                "Express Delivery",
                new BigDecimal("7000"),
                new BigDecimal("20000")
        );

        // Mock DeliveryFeeUpdateResponse 생성
        DeliveryFeeUpdateResponse mockResponse = new DeliveryFeeUpdateResponse(
                "Express Delivery",
                new BigDecimal("7000"),
                new BigDecimal("20000")
        );

        // Mock 서비스 동작 설정
        when(deliveryFeeService.updateDeliveryFee(eq(1L), any(DeliveryFeeUpdateRequest.class))).thenReturn(mockResponse);

        // 테스트 수행
        mockMvc.perform(put("/api/admin/delivery-fee/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Express Delivery"))
                .andExpect(jsonPath("$.fee").value("7000"))
                .andExpect(jsonPath("$.minimumOrderAmount").value("20000"));

        // Verify 서비스 호출 확인
        verify(deliveryFeeService, times(1)).updateDeliveryFee(eq(1L), any(DeliveryFeeUpdateRequest.class));
    }

    @Test
    void testDeleteDeliveryFee() throws Exception {
        // Mock DeliveryFeeDeleteResponse 생성
        DeliveryFeeDeleteResponse mockResponse = new DeliveryFeeDeleteResponse(
                1L,
                "Standard Delivery",
                new BigDecimal("5000"),
                new BigDecimal("30000")
        );

        // Mock 서비스 동작 설정
        when(deliveryFeeService.deleteDeliveryFee(1L)).thenReturn(mockResponse);

        // 테스트 수행
        mockMvc.perform(delete("/api/admin/delivery-fee/1"))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Standard Delivery"))
                .andExpect(jsonPath("$.fee").value("5000"))
                .andExpect(jsonPath("$.minimumOrderAmount").value("30000"));

        // Verify 서비스 호출 확인
        verify(deliveryFeeService, times(1)).deleteDeliveryFee(1L);
    }

    @Test
    void testGetDeliveryFees() throws Exception {
        // Mock DeliveryFeeDetailResponse 리스트 생성
        DeliveryFeeDetailResponse fee1 = new DeliveryFeeDetailResponse(
                1L,
                "Standard Delivery",
                new BigDecimal("5000"),
                new BigDecimal("30000")
        );
        DeliveryFeeDetailResponse fee2 = new DeliveryFeeDetailResponse(
                2L,
                "Express Delivery",
                new BigDecimal("7000"),
                new BigDecimal("20000")
        );

        // Mock Page 생성
        Page<DeliveryFeeDetailResponse> mockPage = new PageImpl<>(List.of(fee1, fee2));

        // Mock 서비스 동작 설정
        when(deliveryFeeService.getDeliveryFees(any(Pageable.class))).thenReturn(mockPage);

        // 테스트 수행
        mockMvc.perform(get("/api/admin/delivery-fee")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("Standard Delivery"))
                .andExpect(jsonPath("$.content[0].fee").value("5000"))
                .andExpect(jsonPath("$.content[0].minimumOrderAmount").value("30000"))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].name").value("Express Delivery"))
                .andExpect(jsonPath("$.content[1].fee").value("7000"))
                .andExpect(jsonPath("$.content[1].minimumOrderAmount").value("20000"));

        // Verify 서비스 호출 확인
        verify(deliveryFeeService, times(1)).getDeliveryFees(any(Pageable.class));
    }

}
