package com.school.kiqa.service;

import com.school.kiqa.command.dto.order.CreateOrUpdateOrderDto;
import com.school.kiqa.command.dto.order.OrderDetailsDto;
import com.school.kiqa.command.dto.orderProduct.CreateOrUpdateOrderProductDto;

public interface OrderService {

    OrderDetailsDto getOrderDetails(Long userId, Long orderId);

    OrderDetailsDto getOrderDetailsNoLogin(Long sessionId, Long orderId);

    OrderDetailsDto addProductToOrder(CreateOrUpdateOrderProductDto orderProductDto, Long orderId, Long userId);

    OrderDetailsDto createOrder(CreateOrUpdateOrderDto orderDto, Long userId);

    OrderDetailsDto createOrderNoLogin(CreateOrUpdateOrderDto orderDto, Long sessionId);

    OrderDetailsDto finishOrderNoLogin(Long orderId, Long sessionId);

    OrderDetailsDto updateProductQuantity(Long orderProductId, Long orderId, int quantity, Long userId);

    OrderDetailsDto removeProductFromOrder(Long orderProductId, Long orderId, Long userId);

    OrderDetailsDto incrementProductQuantity(Long orderProductId, Long orderId, Long userId);

    // order/{}/decrement/{}
    OrderDetailsDto decrementProductQuantity(Long orderProductId, Long orderId, Long userId);

    OrderDetailsDto finishOrder(Long orderId, Long userId);
}
