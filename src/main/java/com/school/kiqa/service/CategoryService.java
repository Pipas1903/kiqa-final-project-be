package com.school.kiqa.service;

import com.school.kiqa.command.dto.CategoryDetailsDto;
import com.school.kiqa.command.dto.CreateOrUpdateCategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDetailsDto createCategory(CreateOrUpdateCategoryDto createOrUpdateCategoryDto);

    List<CategoryDetailsDto> getAllCategories();
}
