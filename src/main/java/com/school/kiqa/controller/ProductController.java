package com.school.kiqa.controller;

import com.school.kiqa.command.Paginated;
import com.school.kiqa.command.dto.product.CreateOrUpdateProductDto;
import com.school.kiqa.command.dto.product.ProductDetailsDto;
import com.school.kiqa.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
public class ProductController {
    private final ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<Paginated<ProductDetailsDto>> getPaginatedFilteredAndSortedProducts(
            @ParameterObject @PageableDefault(page = 1) Pageable pagination,
            @RequestParam(required = false, defaultValue = "0") double minPrice,
            @RequestParam(required = false, defaultValue = "0") double maxPrice,
            @RequestParam(required = false) List<String> category,
            @RequestParam(required = false) List<String> subCategory,
            @RequestParam(required = false) List<String> brands
    ) {
        log.info("received request to get all products");
        PageRequest pageRequest = PageRequest.of(
                pagination.getPageNumber() - 1,
                pagination.getPageSize(),
                //TODO: verify type of sort
                pagination.getSort()
        );

        final var products = productService
                .getAllProducts(pageRequest, brands, subCategory, category, minPrice, maxPrice);
        log.info("products fetched");
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDetailsDto> getProductById(@PathVariable Long id) {
        log.info("received request to get product with id {}", id);
        final var product = productService.getProductById(id);
        log.info("returned product with id {} successfully", id);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/products/{id}")
    @PreAuthorize("@authorized.hasRole('ADMIN')")
    public ResponseEntity<ProductDetailsDto> updateProductById(
            @PathVariable Long id,
            @RequestBody CreateOrUpdateProductDto createOrUpdateProductDto
    ) {
        log.info("received request to update product with id {}", id);
        final var product = productService.updateProductById(id, createOrUpdateProductDto);
        return ResponseEntity.ok(product);
    }

    @PatchMapping("/products/{id}/deactivate")
    @PreAuthorize("@authorized.hasRole('ADMIN')")
    public ProductDetailsDto deactivateProduct(Long id) {
        //TODO: dois endpoints e mandam boolean para 1 só método do service
        return null;
    }

    @PatchMapping("/products/{id}/activate")
    @PreAuthorize("@authorized.hasRole('ADMIN')")
    public ProductDetailsDto activateProduct(Long id) {
        log.info("request received to activate product with id {}", id);
        return null;
    }

}