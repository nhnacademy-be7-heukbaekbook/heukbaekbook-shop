package com.nhnacademy.heukbaekbookshop.order.controller;

import com.nhnacademy.heukbaekbookshop.order.dto.request.OrderCreateRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.request.OrderUpdateRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.response.MyPageRefundableOrderDetailListResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.MyPageRefundableOrderDetailResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.OrderDetailResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.OrderResponse;
import com.nhnacademy.heukbaekbookshop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Long> createOrder(@RequestBody OrderCreateRequest orderCreateRequest) {
        log.info("orderCreateRequest: {}", orderCreateRequest);

        return new ResponseEntity<>(orderService.createOrder(orderCreateRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public OrderResponse getOrders(Pageable pageable) {
        log.info("pageable: {}", pageable);
        return orderService.getOrders(pageable);
    }

    @GetMapping("/{orderId}")
    public OrderDetailResponse getOrderDetailResponse(@PathVariable String orderId,
                                                      @RequestParam(required = false) String email) {

        log.info("orderId: {}, email : {}", orderId, email);
        return orderService.getOrderDetailResponse(orderId);
    }

    @GetMapping("/refundable-orders")
    public ResponseEntity<MyPageRefundableOrderDetailListResponse> getRefundableOrders(@RequestParam(name = "customer-id") String customerId) {
        MyPageRefundableOrderDetailListResponse orders = orderService.getRefundableOrders(customerId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/refundable-orders/{order-id}")
    ResponseEntity<MyPageRefundableOrderDetailResponse> getRefundableOrderDetail(
            @RequestParam(name = "customer-id") String customerId,
            @PathVariable(name = "order-id") Long orderId
    ) {
        MyPageRefundableOrderDetailResponse order = orderService.getRefundableOrderDetail(customerId, orderId);
        return ResponseEntity.ok(order);
    }


    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable String orderId) {
        log.info("orderId: {}", orderId);
        orderService.deleteOrder(orderId);
    }

    @PutMapping("/{orderId}")
    public void updateOrder(@PathVariable String orderId,
                            @RequestBody OrderUpdateRequest orderUpdateRequest) {
        log.info("orderId: {}, orderUpdateRequest: {}", orderId, orderUpdateRequest);
        orderService.updateOrder(orderId, orderUpdateRequest);
    }
}
