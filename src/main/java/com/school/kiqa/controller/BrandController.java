package com.school.kiqa.controller;

import com.school.kiqa.command.Paginated;
import com.school.kiqa.command.dto.BrandDetailsDto;
import com.school.kiqa.command.dto.CreateOrUpdateBrandDto;
import com.school.kiqa.service.BrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @PostMapping("/brands")
    public ResponseEntity<BrandDetailsDto> createBrand(@RequestBody CreateOrUpdateBrandDto dto) {
        log.info("Received request to create brand");
        final var response = brandService.createBrand(dto);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/brands")
    public ResponseEntity<Paginated<BrandDetailsDto>> getAllBrands(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        log.info("Received request to get all brands");
        final var response = brandService.getAllBrands(PageRequest.of(page, size));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/brands/{name}")
    public ResponseEntity<BrandDetailsDto> getBrandByName(@PathVariable String name) {
        log.info("Received request to get brand by name {}", name);
        final var response = brandService.getBrandByName(name);
        return ResponseEntity.ok(response);
    }
}
