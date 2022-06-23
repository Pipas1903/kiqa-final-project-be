package com.school.kiqa.service;

import com.school.kiqa.command.dto.order.CreateOrUpdateOrderDto;
import com.school.kiqa.command.dto.order.OrderDetailsDto;
import com.school.kiqa.command.dto.orderProduct.CreateOrUpdateOrderProductDto;

public interface OrderService {

    OrderDetailsDto addProductToOrder(CreateOrUpdateOrderProductDto orderProductDto, Long orderId);

    OrderDetailsDto createOrder(CreateOrUpdateOrderDto orderDto);

    OrderDetailsDto updateProductQuantity(Long orderProductId, Long orderId, int quantity);

    OrderDetailsDto removeProductFromOrder(Long orderProductId, Long orderId);

    OrderDetailsDto incrementProductQuantity(Long orderProductId, Long orderId);

    // order/{}/decrement/{}
    OrderDetailsDto decrementProductQuantity(Long orderProductId, Long orderId);

    OrderDetailsDto finishOrder(Long orderId);
}
