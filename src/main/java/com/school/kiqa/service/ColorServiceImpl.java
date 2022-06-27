package com.school.kiqa.service;

import com.school.kiqa.command.dto.color.ColorDetailsDto;
import com.school.kiqa.command.dto.color.CreateOrUpdateColorDto;
import com.school.kiqa.converter.ColorConverter;
import com.school.kiqa.exception.alreadyExists.ColorAlreadyExistsException;
import com.school.kiqa.exception.notFound.ColorNotFoundException;
import com.school.kiqa.persistence.entity.ColorEntity;
import com.school.kiqa.persistence.repository.ColorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.school.kiqa.exception.ErrorMessageConstants.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ColorServiceImpl implements ColorService {

    private final ColorRepository colorRepository;
    private final ColorConverter converter;

    @Override
    public ColorDetailsDto createColor(CreateOrUpdateColorDto colorDto) {

        colorRepository.findByHexValue(colorDto.getHexValue())
                .ifPresent(color -> {
                    log.warn(String.format(COLOR_HEX_VALUE_ALREADY_EXISTS, colorDto.getHexValue()));
                    throw new ColorAlreadyExistsException(String.format(COLOR_HEX_VALUE_ALREADY_EXISTS, colorDto.getHexValue()));
                });

        colorRepository.findByColourName(colorDto.getColourName())
                .ifPresent(color -> {
                    log.warn(String.format(COLOR_NAME_ALREADY_EXISTS, colorDto.getColourName()));
                    throw new ColorAlreadyExistsException(String.format(COLOR_NAME_ALREADY_EXISTS, colorDto.getColourName()));
                });

        ColorEntity savedColor = colorRepository.save(converter.convertDtoToColorEntity(colorDto));
        log.info("Saved new color to database");
        return converter.convertEntityToColorDetailsDto(savedColor);
    }

    @Override
    public List<ColorDetailsDto> getAllColors() {
        log.info("returned all colors successfully");
        return colorRepository.findAll()
                .stream()
                .map(converter::convertEntityToColorDetailsDto)
                .collect(Collectors.toList());
    }

    @Override
    public ColorDetailsDto getColorById(Long colorId) {
        ColorEntity colorEntity = colorRepository.findById(colorId)
                .orElseThrow(() -> {
                    log.warn("color with id {} does not exist", colorId);
                    return new ColorNotFoundException(String.format(COLOR_NOT_FOUND_BY_ID, colorId));
                });

        log.info("returned color with id {} successfully", colorId);
        return converter.convertEntityToColorDetailsDto(colorEntity);
    }

    @Override
    public ColorDetailsDto getColorByHexValue(String hexValue) {
        ColorEntity colorEntity = colorRepository.findByHexValue(hexValue)
                .orElseThrow(() -> {
                    log.warn("color with hex value {} does not exist", hexValue);
                    return new ColorNotFoundException(String.format(COLOR_NOT_FOUND_BY_HEX_VALUE, hexValue));
                });

        log.info("returned color with hex value {} successfully", hexValue);
        return converter.convertEntityToColorDetailsDto(colorEntity);
    }
}
