package com.school.kiqa.service;

import com.school.kiqa.command.Paginated;
import com.school.kiqa.command.dto.product.CreateOrUpdateProductDto;
import com.school.kiqa.command.dto.product.ProductDetailsDto;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ProductService {

    Paginated<ProductDetailsDto> getAllProducts(PageRequest pageRequest,
                                                List<String> brands,
                                                List<String> productTypes,
                                                List<String> categories,
                                                double minPrice,
                                                double maxPrice
    );

    ProductDetailsDto getProductById(Long id);

    ProductDetailsDto updateProductById(Long id, CreateOrUpdateProductDto createOrUpdateProductDto);

    List<ProductDetailsDto> searchProductsByName(String name);

    ProductDetailsDto deactivateProduct(Long id);

    ProductDetailsDto activateProduct(Long id);
}