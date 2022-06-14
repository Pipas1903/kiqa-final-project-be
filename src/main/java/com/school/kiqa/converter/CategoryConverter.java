package com.school.kiqa.converter;

import com.school.kiqa.command.dto.CategoryDetailsDto;
import com.school.kiqa.command.dto.CreateOrUpdateCategoryDto;
import com.school.kiqa.persistence.entity.CategoryEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter {
    public CategoryDetailsDto convertEntityToCategoryDetails(CategoryEntity categoryEntity) {
        return CategoryDetailsDto.builder()
                .name(categoryEntity.getName())
                .id(categoryEntity.getId())
                .build();
    }

    public CategoryEntity convertDtoToCategoryEntity(CreateOrUpdateCategoryDto categoryDetailsDto) {
        return CategoryEntity.builder()
                .name(categoryDetailsDto.getName().toLowerCase())
                .build();
    }
}
