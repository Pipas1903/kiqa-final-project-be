package com.school.kiqa.service;

import com.school.kiqa.command.Paginated;
import com.school.kiqa.command.dto.brand.BrandDetailsDto;
import com.school.kiqa.command.dto.brand.CreateOrUpdateBrandDto;
import org.springframework.data.domain.PageRequest;

public interface BrandService {
    BrandDetailsDto createBrand(CreateOrUpdateBrandDto brandDto);

    Paginated<BrandDetailsDto> getAllBrands(PageRequest pageRequest, boolean hasProducts);

    BrandDetailsDto getBrandByName(String name);

    BrandDetailsDto getBrandById(Long brandId);

    BrandDetailsDto updateBrandById(Long brandId, CreateOrUpdateBrandDto createOrUpdateBrandDto);

}
