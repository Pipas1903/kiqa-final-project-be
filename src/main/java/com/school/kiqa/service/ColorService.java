package com.school.kiqa.service;

import com.school.kiqa.command.dto.color.ColorDetailsDto;
import com.school.kiqa.command.dto.color.ColorDto;

import java.util.List;

public interface ColorService {

    ColorDetailsDto createColor(ColorDto colorDto);
    List<ColorDetailsDto> getAllColors();
    ColorDetailsDto getColorById(Long colorId);
    ColorDetailsDto getColorByHexValue(String hexValue);

}
