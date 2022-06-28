package com.school.kiqa.controller;

import com.school.kiqa.command.dto.auth.PrincipalDto;
import com.school.kiqa.command.dto.order.CreateOrUpdateOrderDto;
import com.school.kiqa.command.dto.order.OrderDetailsDto;
import com.school.kiqa.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<OrderDetailsDto> createOrder(@RequestBody CreateOrUpdateOrderDto createOrUpdateOrderDto) {
        log.info("Request received to create order");
        Long userId = ((PrincipalDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        final OrderDetailsDto orderDetailsDto = orderService.createOrder(createOrUpdateOrderDto, userId);
        log.info("Returning created order");
        return ResponseEntity.ok(orderDetailsDto);
    }
}
