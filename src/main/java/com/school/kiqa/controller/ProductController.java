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
            @RequestParam(required = false, defaultValue = "0") Double minPrice,
            @RequestParam(required = false, defaultValue = "0") Double maxPrice,
            @RequestParam(required = false) List<String> category,
            @RequestParam(required = false) List<String> subCategory,
            @RequestParam(required = false) List<String> brands
    ) {
        log.info("Request received to get all products");
        PageRequest pageRequest = PageRequest.of(
                pagination.getPageNumber() - 1,
                pagination.getPageSize(),
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

    @GetMapping("/products/related")
    public ResponseEntity<List<ProductDetailsDto>> getRelatedProducts(
            @RequestParam(required = false, defaultValue = "lips") String categoryName) {
        log.info("Request received to get products related by category {}", categoryName);
        final var products = productService.getRelatedProducts(categoryName);
        log.info("products related to {} fetched", categoryName);
        return ResponseEntity.ok(products);
    }

    @PutMapping("/products/{id}")
    @PreAuthorize("@authorized.hasRole('ADMIN')")
    public ResponseEntity<ProductDetailsDto> updateProductById(
            @PathVariable Long id,
            @RequestBody CreateOrUpdateProductDto createOrUpdateProductDto
    ) {
        log.info("received request to update product with id {}", id);
        final var product = productService.updateProductById(id, createOrUpdateProductDto);
        log.info("product with id {} was successfully updated", id);
        return ResponseEntity.ok(product);
    }

    @PatchMapping("/products/{id}/deactivate")
    @PreAuthorize("@authorized.hasRole('ADMIN')")
    public ResponseEntity<ProductDetailsDto> deactivateProduct(@PathVariable Long id, boolean activeProduct) {
        log.info("request received to deactivate product with id {}", id);
        final var changedProduct = productService.activateOrDeactivateProduct(id, activeProduct);
        log.info("deactivated product with id {} successfully", id);
        return ResponseEntity.ok(changedProduct);
    }

    @PatchMapping("/products/{id}/activate")
    @PreAuthorize("@authorized.hasRole('ADMIN')")
    public ResponseEntity<ProductDetailsDto> activateProduct(@PathVariable Long id, boolean activeProduct) {
        log.info("request received to activate product with id {}", id);
        final var changedProduct = productService.activateOrDeactivateProduct(id, activeProduct);
        log.info("activated product with id {} successfully", id);
        return ResponseEntity.ok(changedProduct);
    }

    @GetMapping("/products/search")
    public ResponseEntity<Paginated<ProductDetailsDto>> searchProductByName(
            @RequestParam String name,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        log.info("Request received to get products by name containing '{}'", name);
        final PageRequest pageRequest = PageRequest.of(page, size);
        final var products = productService.searchProductsByName(name, pageRequest);
        log.info("Returning products that have '{}' in the name", name);
        return ResponseEntity.ok(products);
    }
}
