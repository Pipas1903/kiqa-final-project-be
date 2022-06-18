package com.school.kiqa.controller;

import com.school.kiqa.command.dto.category.CategoryDetailsDto;
import com.school.kiqa.command.dto.category.CreateOrUpdateCategoryDto;
import com.school.kiqa.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/categories")
    public ResponseEntity<CategoryDetailsDto> createCategory(@RequestBody CreateOrUpdateCategoryDto dto) {
        log.info("Request received to create new category");
        return ResponseEntity.ok(categoryService.createCategory(dto));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDetailsDto>> getAllCategories() {
        log.info("Request received to get all categories");
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

}
