package com.school.kiqa.service;

import com.school.kiqa.command.Paginated;
import com.school.kiqa.command.dto.BrandDetailsDto;
import com.school.kiqa.command.dto.CreateOrUpdateBrandDto;
import org.springframework.data.domain.PageRequest;

public interface BrandService {
    BrandDetailsDto createBrand(CreateOrUpdateBrandDto brandDto);

    Paginated<BrandDetailsDto> getAllBrands(PageRequest pageRequest);

    BrandDetailsDto getBrandByName(String name);
}
