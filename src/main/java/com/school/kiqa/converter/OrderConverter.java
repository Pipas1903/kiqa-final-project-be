package com.school.kiqa.converter;

import com.school.kiqa.command.dto.order.CreateOrUpdateOrderDto;
import com.school.kiqa.command.dto.order.OrderDetailsDto;
import com.school.kiqa.persistence.entity.OrderEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class OrderConverter {

    public OrderDetailsDto convertEntityToOrderDetailsDto(OrderEntity orderEntity) {
        return OrderDetailsDto.builder()
                .id(orderEntity.getId())
                .userId(orderEntity.getUserEntity().getId())
                .totalPrice(orderEntity.getTotalPrice())
                .creationDate(orderEntity.getCreationDate())
                .status(orderEntity.getStatus())
                .build();
    }

    public OrderEntity convertDtoToOrderEntity(CreateOrUpdateOrderDto orderDto) {
        return OrderEntity.builder()
                .creationDate(LocalDate.now())
                .build();
    }
}
