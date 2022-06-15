package com.school.kiqa.controller;

import com.school.kiqa.command.Paginated;
import com.school.kiqa.command.dto.ProductDetailsDto;
import com.school.kiqa.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
public class ProductController {
    private final ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<Paginated<ProductDetailsDto>> getPaginatedProducts(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "20", required = false) int size,
            @RequestParam(defaultValue = "0", required = false) double maxPrice,
            @RequestParam(defaultValue = "0", required = false) double minPrice,
            @RequestParam(required = false) List<String> category,
            @RequestParam(required = false) List<String> productType,
            @RequestParam(required = false) List<String> brand) {
        log.info("received request to get all products");
        final var products = productService.getAllProducts(PageRequest.of(page, size));
        log.info("products fetched");
        return ResponseEntity.ok(products);
    }
}