package com.school.kiqa.adapter;

import com.school.kiqa.persistence.entity.ProductEntity;

public class ProductApiConverter {
    public static ProductEntity convertProductApiToProductEntity(ProductFromApi product) {
        return ProductEntity.builder()
                .price(product.getPrice())
                .description(product.getDescription())
                .image(product.getApi_featured_image())
                .isActive(true)
                .name(product.getName())
                .build();
    }
}
