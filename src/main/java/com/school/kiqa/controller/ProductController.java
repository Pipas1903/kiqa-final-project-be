package com.school.kiqa.controller;

import com.school.kiqa.command.Paginated;
import com.school.kiqa.command.dto.CreateOrUpdateProductDto;
import com.school.kiqa.command.dto.ProductDetailsDto;
import com.school.kiqa.exception.ProductNotFoundException;
import com.school.kiqa.persistence.entity.ProductEntity;
import com.school.kiqa.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.*;
import java.util.List;

import static com.school.kiqa.exception.ErrorMessageConstants.PRODUCT_NOT_FOUND;

@RequiredArgsConstructor
@Slf4j
@RestController
public class ProductController {
    private final ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<Paginated<ProductDetailsDto>> getPaginatedAndFilteredProducts(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "0", required = false) double maxPrice,
            @RequestParam(defaultValue = "0", required = false) double minPrice,
            @RequestParam(required = false) List<String> category,
            @RequestParam(required = false) List<String> productType,
            @RequestParam(required = false) List<String> brand,
            @RequestParam(defaultValue = "UNSORTED", required = false) SortOrder alphabeticalOrder,
            @RequestParam(defaultValue = "UNSORTED", required = false) SortOrder price
    ) {
        log.info("received request to get all products");
        final var products = productService.getAllProducts(PageRequest.of(page, size));
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
    public ResponseEntity<ProductDetailsDto> updateProductById(
            @PathVariable Long id,
            @RequestBody CreateOrUpdateProductDto createOrUpdateProductDto
    ) {
        log.info("received request to update product with id {}", id);
        final var product = productService.updateProductById(id, createOrUpdateProductDto);
        return ResponseEntity.ok(product);
    }

    @PatchMapping("/products/{id}/deactivate")
    public ProductDetailsDto deactivateProduct(Long id) {
        //TODO: dois endpoints e mandam boolean
        return null;
    }

    @PatchMapping("/products/{id}/activate")
    public ProductDetailsDto activateProduct(Long id) {
        return null;
    }

}