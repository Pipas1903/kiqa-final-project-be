package com.school.kiqa.converter;

import com.school.kiqa.command.dto.color.ColorDto;
import com.school.kiqa.persistence.entity.ColorEntity;
import org.springframework.stereotype.Component;

@Component
public class ColorConverter {
    public ColorEntity convertDtoToColorEntity(ColorDto colorDto) {
        return ColorEntity.builder()
                .hexValue(colorDto.getHexValue())
                .colorName(colorDto.getColourName())
                .build();
    }

    public ColorDto convertEntityToColorDto(ColorEntity colorEntity) {
        return ColorDto.builder()
                .colourName(colorEntity.getColorName())
                .hexValue(colorEntity.getHexValue())
                .build();
    }
}
