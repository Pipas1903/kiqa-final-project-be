package com.school.kiqa.command.dto.orderProduct;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrUpdateOrderProductDto {
    private Integer quantity;
    private Long productId;
    private Long orderId;
    private Long colorId;
}
