package com.school.kiqa.controller;

import com.school.kiqa.command.dto.category.CategoryDetailsDto;
import com.school.kiqa.command.dto.category.CreateOrUpdateCategoryDto;
import com.school.kiqa.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/categories")
    @PreAuthorize("@authorized.hasRole('ADMIN')")
    public ResponseEntity<CategoryDetailsDto> createCategory(@RequestBody CreateOrUpdateCategoryDto dto) {
        log.info("Request received to create new category");
        final var category = categoryService.createCategory(dto);
        log.info("Request received to create new category");
        return ResponseEntity.ok(category);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDetailsDto>> getAllCategories() {
        log.info("Request received to get all categories");
        final var categories = categoryService.getAllCategories();
        log.info("returned all categories successfully");
        return ResponseEntity.ok(categories);
    }


    @GetMapping("/categories/{id}")
    public ResponseEntity<CategoryDetailsDto> getCategoryById(@PathVariable Long id) {
        log.info("Request received to get category with id {}", id);
        CategoryDetailsDto category = categoryService.getCategoryById(id);
        log.info("Returned category with id {}", id);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/categories/{name}")
    public ResponseEntity<CategoryDetailsDto> getCategoryByName(@PathVariable String name) {
        log.info("Request received to get category with name {}", name);
        CategoryDetailsDto category = categoryService.getCategoryByName(name);
        log.info("Returned category with name {}", name);
        return ResponseEntity.ok(category);
    }

    @PutMapping("/categories/{id}")
    @PreAuthorize("@authorized.hasRole('ADMIN')")
    public ResponseEntity<CategoryDetailsDto> updateCategoryById(@PathVariable Long id,
                                                         @RequestBody CreateOrUpdateCategoryDto createOrUpdateCategoryDto) {
        log.info("Request received to update category with id {}", id);
        CategoryDetailsDto category = categoryService.updateCategoryById(id, createOrUpdateCategoryDto);
        log.info("Returned updated category with id {}", id);
        return ResponseEntity.ok(category);
    }

}
