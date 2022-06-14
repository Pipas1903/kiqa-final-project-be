package com.school.kiqa.adapter;

import com.school.kiqa.persistence.entity.ProductEntity;

public class ProductApiConverter {
    public static ProductEntity convertProductApiToProductEntity(ProductFromApi product) {
        return ProductEntity.builder()
                .price(product.getPrice())
                .description(product.getDescription())
                .image(product.getImage_link())
                .isActive(true)
                .name(product.getName())
                .build();
    }
}