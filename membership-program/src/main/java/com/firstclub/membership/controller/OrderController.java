package com.firstclub.membership.controller;

import com.firstclub.membership.domain.dto.OrderRequest;
import com.firstclub.membership.domain.entity.UserOrder;
import com.firstclub.membership.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> placeOrder(@Valid @RequestBody OrderRequest request) {
        UserOrder order = orderService.placeOrder(request);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("orderId", order.getId());
        body.put("userId", request.userId());
        body.put("amount", order.getAmount());
        body.put("createdAt", order.getCreatedAt());
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }
}
