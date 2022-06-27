package com.school.kiqa.controller;

import com.school.kiqa.command.dto.color.ColorDetailsDto;
import com.school.kiqa.command.dto.color.CreateOrUpdateColorDto;
import com.school.kiqa.service.ColorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
public class ColorController {

    private final ColorService colorService;

    @PostMapping("/colors")
    @PreAuthorize("@authorized.hasRole('ADMIN')")
    public ResponseEntity<ColorDetailsDto> createColor(@Valid @RequestBody CreateOrUpdateColorDto colorDto) {
        log.info("Request received to create color with name {} and hex value {}", colorDto.getColourName(), colorDto.getHexValue());
        ColorDetailsDto color = colorService.createColor(colorDto);
        log.info("Returning created color");
        return ResponseEntity.ok(color);
    }

    @GetMapping("/colors")
    public ResponseEntity<List<ColorDetailsDto>> getAllColors() {
        log.info("Request received to get all colors");
        List<ColorDetailsDto> colors = colorService.getAllColors();
        log.info("Returning colors");
        return ResponseEntity.ok(colors);
    }

    @GetMapping("/colors/{id}")
    public ResponseEntity<ColorDetailsDto> getColorById(@PathVariable Long id) {
        log.info("Request received to get color with id {}", id);
        ColorDetailsDto color = colorService.getColorById(id);
        log.info("Returned color with id {}", id);
        return ResponseEntity.ok(color);
    }

    @PutMapping("/colors/{hexValue}")
    public ResponseEntity<ColorDetailsDto> getColorByHexValue(@PathVariable String hexValue) {
        log.info("Request received to get color with hexValue {}", hexValue);
        ColorDetailsDto color = colorService.getColorByHexValue(hexValue);
        log.info("Returned color with hexValue {}", hexValue);
        return ResponseEntity.ok(color);
    }
}
