package com.school.kiqa.converter;

import com.school.kiqa.command.dto.color.ColorDetailsDto;
import com.school.kiqa.command.dto.color.CreateOrUpdateColorDto;
import com.school.kiqa.persistence.entity.ColorEntity;
import org.springframework.stereotype.Component;

@Component
public class ColorConverter {
    public ColorEntity convertDtoToColorEntity(CreateOrUpdateColorDto colorDto) {
        return ColorEntity.builder()
                .hexValue(colorDto.getHexValue())
                .colorName(colorDto.getColourName())
                .build();
    }

    public ColorDetailsDto convertEntityToColorDetailsDto(ColorEntity colorEntity) {
        return ColorDetailsDto.builder()
                .id(colorEntity.getId())
                .colourName(colorEntity.getColorName())
                .hexValue(colorEntity.getHexValue())
                .build();
    }
}
