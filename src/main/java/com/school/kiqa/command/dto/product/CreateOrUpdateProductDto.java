package com.school.kiqa.command.dto.product;

import com.school.kiqa.command.dto.color.ColorDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
@Builder
public class CreateOrUpdateProductDto {

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
