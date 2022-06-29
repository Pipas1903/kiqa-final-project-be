package com.school.kiqa.service;

import com.school.kiqa.command.dto.category.CategoryDetailsDto;
import com.school.kiqa.command.dto.category.CreateOrUpdateCategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDetailsDto createCategory(CreateOrUpdateCategoryDto createOrUpdateCategoryDto);

    List<CategoryDetailsDto> getAllCategories();

    CategoryDetailsDto getCategoryById(Long categoryId);

    CategoryDetailsDto getCategoryByName(String categoryName);

    CategoryDetailsDto updateCategoryById(Long categoryId, CreateOrUpdateCategoryDto createOrUpdateCategoryDto);
}
