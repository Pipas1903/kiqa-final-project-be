package com.school.kiqa.service;

import com.school.kiqa.command.dto.color.ColorDto;

import java.util.List;

public interface ColorService {
    ColorDto createColor(ColorDto createOrUpdateCategoryDto);

    List<ColorDto> getAllColors();


}
