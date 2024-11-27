package com.nhnacademy.heukbaekbookshop.order.controller;

import com.nhnacademy.heukbaekbookshop.order.controller.DeliveryFeeController;
import com.nhnacademy.heukbaekbookshop.order.service.DeliveryFeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeliveryFeeController.class)
class DeliveryFeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeliveryFeeService deliveryFeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetDeliveryFeeByMinimumOrderAmount() throws Exception {
        // Mock 서비스 동작 설정
        BigDecimal mockFee = new BigDecimal("5000");
        when(deliveryFeeService.getDeliveryFeeByMinimumOrderAmount(any(BigDecimal.class))).thenReturn(mockFee);

        // 테스트 수행
        mockMvc.perform(get("/api/delivery-fees")
                        .param("minimumOrderAmount", "30000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("5000"));

        // Verify 서비스 호출 확인
        verify(deliveryFeeService, times(1)).getDeliveryFeeByMinimumOrderAmount(new BigDecimal("30000"));
    }
}
