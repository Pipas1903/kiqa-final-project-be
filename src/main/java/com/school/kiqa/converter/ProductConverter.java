package com.school.kiqa.converter;

import com.school.kiqa.command.dto.product.CreateOrUpdateProductDto;
import com.school.kiqa.command.dto.product.ProductDetailsDto;
import com.school.kiqa.persistence.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductConverter {
    private final ColorConverter converter;

    public ProductEntity convertDtoToProductEntity(CreateOrUpdateProductDto dto) {
        return ProductEntity.builder()
                .price(dto.getPrice())
                .description(dto.getDescription())
                .name(dto.getName())
                .isActive(dto.getIsActive())
                .image(dto.getImage())
                .build();
    }

    public ProductDetailsDto convertEntityToProductDetailsDto(ProductEntity product) {
        return ProductDetailsDto.builder()
                .id(product.getId())
                .name(product.getName())
                .brand(product.getBrandEntity().getName())
                .description(product.getDescription())
                .image(product.getImage())
                .isActive(product.getIsActive())
                .categoryName(product.getCategoryEntity().getName())
                .productTypeName(product.getProductTypeEntity().getName())
                .price(product.getPrice())
                .build();
    }
}