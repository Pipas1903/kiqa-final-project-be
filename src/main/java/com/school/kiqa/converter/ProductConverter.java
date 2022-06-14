package com.school.kiqa.converter;

import com.school.kiqa.command.dto.CreateOrUpdateProductDto;
import com.school.kiqa.command.dto.ProductDetailsDto;
import com.school.kiqa.persistence.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductConverter {
    private final ColorConverter converter;

    public ProductEntity convertDtoToProductEntity(CreateOrUpdateProductDto dto) {
        return ProductEntity.builder().build();
    }

    public ProductDetailsDto convertEntityToProductDetailsDto(ProductEntity product) {
        return ProductDetailsDto.builder()
                .id(product.getId())
                .brand(product.getBrandEntity().getName())
                .description(product.getDescription())
                .image(product.getImage())
                .isActive(product.getIsActive())
                .categoryName(product.getCategoryEntity().getName())
                .price(product.getPrice())
                .colors(product.getColors().stream()
                        .map(converter::convertEntityToColorDto)
                        .collect(Collectors.toList()))
                .name(product.getName())
                .build();
    }
}