package com.school.kiqa.service;

import com.school.kiqa.command.Paginated;
import com.school.kiqa.command.dto.product.CreateOrUpdateProductDto;
import com.school.kiqa.command.dto.product.ProductDetailsDto;
import com.school.kiqa.persistence.entity.ProductEntity;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ProductService {

    Paginated<ProductDetailsDto> getAllProducts(PageRequest pageRequest,
                                                List<String> brands,
                                                List<String> productTypes,
                                                List<String> categories,
                                                Double minPrice,
                                                Double maxPrice
    );

    ProductDetailsDto getProductById(Long id);

    ProductDetailsDto updateProductById(Long id, CreateOrUpdateProductDto createOrUpdateProductDto);

    ProductDetailsDto activateOrDeactivateProduct(Long id, boolean activateProduct);

    ProductDetailsDto activateProduct(ProductEntity productEntity, Long id);

    ProductDetailsDto deactivateProduct(ProductEntity productEntity, Long id);

    Paginated<ProductDetailsDto> searchProductsByName(String name, PageRequest pageRequest);
 
    List<ProductDetailsDto> getRelatedProducts(String categoryName);
}
