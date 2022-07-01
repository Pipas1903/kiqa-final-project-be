package com.school.kiqa.controller;

import com.school.kiqa.command.dto.auth.PrincipalDto;
import com.school.kiqa.command.dto.order.CreateOrUpdateOrderDto;
import com.school.kiqa.command.dto.order.OrderDetailsDto;
import com.school.kiqa.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/orders")
    @PreAuthorize("@authorized.hasRole('USER')")
    public ResponseEntity<OrderDetailsDto> createOrder(@RequestBody CreateOrUpdateOrderDto createOrUpdateOrderDto) {
        log.info("Request received to create order");
        PrincipalDto user = ((PrincipalDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if (user.getName().equals("x-session")) {
            final OrderDetailsDto orderDetailsDto = orderService.createOrderNoLogin(createOrUpdateOrderDto, user.getId());
            log.info("Returning created order - session");
            return ResponseEntity.ok(orderDetailsDto);
        }
        final OrderDetailsDto orderDetailsDto = orderService.createOrder(createOrUpdateOrderDto, user.getId());
        log.info("Returning created order for user {}", user.getId());
        return ResponseEntity.ok(orderDetailsDto);
    }

    @PreAuthorize("@authorized.hasRole('USER')")
    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderDetailsDto> getOrderById(@PathVariable Long id) {
        log.info("Request get order with id {}", id);
        PrincipalDto user = ((PrincipalDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if (user.getName().equals("x-session")) {
            final OrderDetailsDto orderDetailsDto = orderService.getOrderDetailsNoLogin(user.getId(), id);
            log.info("Returning order for session with {}", id);
            return ResponseEntity.ok(orderDetailsDto);
        }
        final OrderDetailsDto orderDetailsDto = orderService.getOrderDetails(user.getId(), id);
        log.info("Returning order with id {} for user {}", id, user.getId());
        return ResponseEntity.ok(orderDetailsDto);
    }
}
