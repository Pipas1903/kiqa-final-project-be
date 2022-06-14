package com.school.kiqa.service;

import com.school.kiqa.command.Paginated;
import com.school.kiqa.command.dto.CreateOrUpdateProductDto;
import com.school.kiqa.command.dto.ProductDetailsDto;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Paginated<ProductDetailsDto> getAllProducts(PageRequest pageRequest);

    ProductDetailsDto getProductById(Long id);

    Paginated<ProductDetailsDto> getProductsByBrand(String brand, PageRequest pageRequest);

    ProductDetailsDto updateProductById(Long id, CreateOrUpdateProductDto createOrUpdateProductDto);

    ProductDetailsDto deactivateProduct(Long id);
}