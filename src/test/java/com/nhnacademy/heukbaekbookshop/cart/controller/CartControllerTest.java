package com.nhnacademy.heukbaekbookshop.cart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.heukbaekbookshop.cart.dto.CartBookSummaryResponse;
import com.nhnacademy.heukbaekbookshop.cart.dto.CartCreateRequest;
import com.nhnacademy.heukbaekbookshop.cart.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCart() throws Exception {
        // Given
        Long customerId = 1L;
        List<CartCreateRequest> cartCreateRequests = List.of(
                new CartCreateRequest(101L, 2),
                new CartCreateRequest(102L, 1)
        );

        doNothing().when(cartService).createCart(customerId, cartCreateRequests);

        // When & Then
        mockMvc.perform(post("/api/carts")
                        .header("X-USER-ID", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartCreateRequests)))
                .andExpect(status().isCreated()); // 응답 상태 코드 확인

        verify(cartService).createCart(customerId, cartCreateRequests); // 서비스 호출 검증
    }

    @Test
    void getCartBooks() throws Exception {
        //given
        Long customerId = 1L;

        List<CartBookSummaryResponse> cartBookSummaryResponses = List.of(
                new CartBookSummaryResponse(1L, 1),
                new CartBookSummaryResponse(2L, 2)
        );

        when(cartService.getCartBooks(customerId)).thenReturn(cartBookSummaryResponses);

        //when & then
        mockMvc.perform(get("/api/carts")
                .header("X-USER-ID", customerId))
                .andExpect(status().isOk());

        verify(cartService).getCartBooks(customerId);
    }
}
