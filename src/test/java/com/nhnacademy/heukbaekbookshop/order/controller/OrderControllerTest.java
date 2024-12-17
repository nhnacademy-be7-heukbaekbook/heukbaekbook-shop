package com.nhnacademy.heukbaekbookshop.order.controller;

import com.nhnacademy.heukbaekbookshop.order.domain.OrderStatus;
import com.nhnacademy.heukbaekbookshop.order.dto.request.OrderBookRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.request.OrderCreateRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.request.OrderUpdateRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.response.*;
import com.nhnacademy.heukbaekbookshop.order.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    void createOrder_Success() throws Exception {
        // Given
        OrderCreateRequest request = new OrderCreateRequest(
                1L, "10000", "customer@example.com", "John Doe", "123456789",
                "toss-123", "Jane Doe", "12345", "Road Name", "Detail Address",
                "987654321", "3000", "0", List.of(new OrderBookRequest(1L, 2, "10000", false, null))
        );
        Long createdOrderId = 1L;

        when(orderService.createOrder(any(OrderCreateRequest.class))).thenReturn(createdOrderId);

        // When & Then
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "customerId": 1,
                                    "totalPrice": "10000",
                                    "customerEmail": "customer@example.com",
                                    "customerName": "John Doe",
                                    "customerPhoneNumber": "123456789",
                                    "tossOrderId": "toss-123",
                                    "recipient": "Jane Doe",
                                    "postalCode": "12345",
                                    "roadNameAddress": "Road Name",
                                    "detailAddress": "Detail Address",
                                    "recipientPhoneNumber": "987654321",
                                    "deliveryFee": "3000",
                                    "usedPoint": "0",
                                    "orderBookRequests": [{"bookId": 1, "quantity": 2}]
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(content().string(createdOrderId.toString()));

        verify(orderService, times(1)).createOrder(any(OrderCreateRequest.class));
    }

    @Test
    void getOrders_Success() throws Exception {
        // Given
        DeliverySummaryResponse deliverySummaryResponse = new DeliverySummaryResponse("정동현");
        Page<OrderSummaryResponse> page = new PageImpl<>(List.of(new OrderSummaryResponse(LocalDate.now(), "tossOrderId", "title", OrderStatus.DELIVERED.getKorean(), "홍길동", "13000/1", deliverySummaryResponse)));
        OrderResponse response = new OrderResponse(page);

        when(orderService.getOrders(any(Pageable.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/orders")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderSummaryResponsePage.content[0].tossOrderId").value("tossOrderId"))
                .andExpect(jsonPath("$.orderSummaryResponsePage.content[0].title").value("title"));

        verify(orderService, times(1)).getOrders(any(Pageable.class));
    }

    @Test
    void getOrderDetailResponse_Success() throws Exception {
        // Given
        String orderId = "order-123";
        OrderDetailResponse response = new OrderDetailResponse(
                "toss-123", "John Doe", "3000", "10000", "Card", "Jane Doe",
                12345L, "Road Name", "Detail Address", "8000", "2000", "10000", "1000",
                "Paid", List.of(new OrderBookResponse(1L, "thumbnailUrl", "title", "10000", 5, "9000", BigDecimal.valueOf(10), "45000"))
        );

        when(orderService.getOrderDetailResponse(orderId)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/orders/{orderId}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("John Doe"))
                .andExpect(jsonPath("$.books[0].quantity").value(5));

        verify(orderService, times(1)).getOrderDetailResponse(orderId);
    }

    @Test
    void updateOrder_Success() throws Exception {
        // Given
        String orderId = "order-123";
        OrderUpdateRequest request = new OrderUpdateRequest("Shipped");

        doNothing().when(orderService).updateOrder(eq(orderId), any(OrderUpdateRequest.class));

        // When & Then
        mockMvc.perform(put("/api/orders/{orderId}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "status": "Shipped"
                                }
                                """))
                .andExpect(status().isOk());

        verify(orderService, times(1)).updateOrder(eq(orderId), any(OrderUpdateRequest.class));
    }

    @Test
    void deleteOrder_Success() throws Exception {
        // Given
        String orderId = "order-123";

        doNothing().when(orderService).deleteOrder(orderId);

        // When & Then
        mockMvc.perform(delete("/api/orders/{orderId}", orderId))
                .andExpect(status().isNoContent());

        verify(orderService, times(1)).deleteOrder(orderId);
    }
}