package com.school.kiqa.command.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class ProductDetailsDto {
    private Long id;

    private Double price;

    private String description;

    private String brand;

    private String name;

    private Boolean isActive;

    private String image;

    private String categoryName;

    private String subCategoryName;

    private List<ColorDto> colors;
}
