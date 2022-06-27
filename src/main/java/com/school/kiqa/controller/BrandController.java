package com.school.kiqa.controller;

import com.school.kiqa.command.Paginated;
import com.school.kiqa.command.dto.brand.BrandDetailsDto;
import com.school.kiqa.command.dto.brand.CreateOrUpdateBrandDto;
import com.school.kiqa.service.BrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @PostMapping("/brands")
    @PreAuthorize("@authorized.hasRole('ADMIN')")
    public ResponseEntity<BrandDetailsDto> createBrand(@RequestBody CreateOrUpdateBrandDto dto) {
        log.info("Received request to create brand");
        final var response = brandService.createBrand(dto);
        log.info("brand created successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/brands")
    public ResponseEntity<Paginated<BrandDetailsDto>> getAllBrands(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "true", required = false) boolean hasProducts
    ) {
        log.info("Received request to get all brands");
        final var response = brandService.getAllBrands(PageRequest.of(page, size), hasProducts);
        log.info("Returned all brands successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/brands/{name}")
    public ResponseEntity<BrandDetailsDto> getBrandByName(@PathVariable String name) {
        log.info("Received request to get brand by name {}", name);
        final var response = brandService.getBrandByName(name);
        log.info("returned brand with name {} successfully", name);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/brands/{id}")
    public ResponseEntity<BrandDetailsDto> getBrandById(@PathVariable Long id) {
        log.info("Received request to get brand by id {}", id);
        final var response = brandService.getBrandById(id);
        log.info("returned brand with id {} successfully", id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/brands/{id}")
    @PreAuthorize("@authorized.hasRole('ADMIN')")
    public ResponseEntity<BrandDetailsDto> updateBrandById(@PathVariable Long id, CreateOrUpdateBrandDto createOrUpdateBrandDto) {
        log.info("Received request to update brand by id {}", id);
        final var response = brandService.updateBrandById(id, createOrUpdateBrandDto);
        log.info("returned brand with id {} successfully", id);
        return ResponseEntity.ok(response);
    }
}
