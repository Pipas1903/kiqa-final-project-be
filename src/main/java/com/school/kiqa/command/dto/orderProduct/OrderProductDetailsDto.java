package com.school.kiqa.command.dto.orderProduct;

import com.school.kiqa.command.dto.color.ColorDetailsDto;
import com.school.kiqa.command.dto.product.ProductDetailsDto;
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
public class OrderProductDetailsDto {
    private Integer quantity;
    private Long productId;
    private Long orderId;
    private ColorDetailsDto colorDto;
    private ProductDetailsDto productDetailsDto;
}
