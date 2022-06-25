package com.school.kiqa.service;

import com.school.kiqa.command.dto.category.CategoryDetailsDto;
import com.school.kiqa.command.dto.category.CreateOrUpdateCategoryDto;
import com.school.kiqa.converter.CategoryConverter;
import com.school.kiqa.exception.alreadyExists.AlreadyExistsException;
import com.school.kiqa.exception.notFound.CategoryNotFoundException;
import com.school.kiqa.persistence.entity.CategoryEntity;
import com.school.kiqa.persistence.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.school.kiqa.exception.ErrorMessageConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryConverter converter;

    @Override
    public CategoryDetailsDto createCategory(CreateOrUpdateCategoryDto dto) {

        if (categoryRepository.isCategoryNameUnavailable(dto.getName())) {
            log.error(String.format("Encountered category with same name as %s.", dto.getName()));
            throw new AlreadyExistsException(String.format(
                    CATEGORY_ALREADY_EXISTS,
                    dto.getName())
            );
        }

        final CategoryEntity category = converter.convertDtoToCategoryEntity(dto);
        final CategoryEntity savedCategory = categoryRepository.save(category);

        log.info("Category saved to database");

        return converter.convertEntityToCategoryDetails(savedCategory);
    }

    @Override
    public List<CategoryDetailsDto> getAllCategories() {
        log.info("returned all categories successfully");
        return categoryRepository.findAll().stream()
                .map(converter::convertEntityToCategoryDetails)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDetailsDto getCategoryById(Long categoryId) {
        CategoryEntity categoryEntity = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.warn("category with id {} does not exist", categoryId);
                    return new CategoryNotFoundException(String.format(CATEGORY_NOT_FOUND_BY_ID, categoryId));
                });

        log.info("returned category with id {} successfully", categoryId);
        return converter.convertEntityToCategoryDetails(categoryEntity);
    }

    @Override
    public CategoryDetailsDto getCategoryByName(String categoryName) {
        CategoryEntity categoryEntity = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> {
                    log.warn("category with name {} does not exist", categoryName);
                    return new CategoryNotFoundException(String.format(CATEGORY_NOT_FOUND_BY_NAME, categoryName));
                });

        log.info("returned category with name {} successfully", categoryName);
        return converter.convertEntityToCategoryDetails(categoryEntity);
    }

    @Override
    public CategoryDetailsDto updateCategoryById(Long categoryId, CreateOrUpdateCategoryDto createOrUpdateCategoryDto) {
        CategoryEntity categoryEntity = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.warn("category with id {} does not exist", categoryId);
                    return new CategoryNotFoundException(String.format(CATEGORY_NOT_FOUND_BY_ID, categoryId));
                });

        CategoryEntity category = converter.convertDtoToCategoryEntity(createOrUpdateCategoryDto);
        category.setId(categoryId);

        final var savedCategory = categoryRepository.save(category);
        log.info("Saved updated category with id {} to database", savedCategory.getId());

        log.info("category with id {} was successfully updated", categoryId);
        return converter.convertEntityToCategoryDetails(category);
    }
}