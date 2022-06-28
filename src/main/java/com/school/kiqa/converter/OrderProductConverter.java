package com.school.kiqa.converter;

import com.school.kiqa.command.dto.orderProduct.CreateOrUpdateOrderProductDto;
import com.school.kiqa.command.dto.orderProduct.OrderProductDetailsDto;
import com.school.kiqa.persistence.entity.OrderProductEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderProductConverter {

    public OrderProductEntity convertDtoToOrderProductEntity(CreateOrUpdateOrderProductDto dto) {
        return OrderProductEntity.builder()
                .quantity(dto.getQuantity())
                .build();
    }

    public OrderProductDetailsDto convertEntityToOrderProductDetailsDto(OrderProductEntity entity) {
        return OrderProductDetailsDto.builder()
                .productId(entity.getProduct().getId())
                .quantity(entity.getQuantity())
                .orderId(entity.getOrderEntity().getId())
                .build();
    }

}
