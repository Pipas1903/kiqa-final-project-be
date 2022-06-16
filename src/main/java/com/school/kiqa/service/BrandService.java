package com.school.kiqa.service;

import com.school.kiqa.command.Paginated;
import com.school.kiqa.command.dto.BrandDetailsDto;
import com.school.kiqa.command.dto.CreateOrUpdateBrandDto;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface BrandService {
    BrandDetailsDto createBrand(CreateOrUpdateBrandDto brandDto);

    Paginated<BrandDetailsDto> getAllBrands(PageRequest pageRequest, boolean hasProducts);

    BrandDetailsDto getBrandByName(String name);
}
