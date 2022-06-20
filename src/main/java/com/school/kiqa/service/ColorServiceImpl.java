package com.school.kiqa.service;

import com.school.kiqa.converter.ColorConverter;
import com.school.kiqa.command.dto.color.ColorDto;
import com.school.kiqa.persistence.entity.ColorEntity;
import com.school.kiqa.persistence.repository.ColorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ColorServiceImpl implements ColorService {

    private final ColorRepository colorRepository;
    private final ColorConverter converter;

    @Override
    public ColorDto createColor(ColorDto createOrUpdateCategoryDto) {

        ColorEntity savedColor = colorRepository.save(converter.convertDtoToColorEntity(createOrUpdateCategoryDto));

        log.info("Saved new color to database");

        return converter.convertEntityToColorDto(savedColor);
    }

    @Override
    public List<ColorDto> getAllColors() {
        log.info("returned all colors successfully");
        return colorRepository.findAll()
                .stream()
                .map(converter::convertEntityToColorDto)
                .collect(Collectors.toList());
    }

}
