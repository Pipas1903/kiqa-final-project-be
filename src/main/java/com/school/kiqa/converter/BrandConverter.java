package com.school.kiqa.converter;

import com.school.kiqa.command.dto.brand.BrandDetailsDto;
import com.school.kiqa.command.dto.brand.CreateOrUpdateBrandDto;
import com.school.kiqa.persistence.entity.BrandEntity;
import org.springframework.stereotype.Component;

@Component
public class BrandConverter {
    public BrandDetailsDto convertEntityToBrandDetailsDto(BrandEntity brand) {
        return BrandDetailsDto.builder()
                .id(brand.getId())
                .name(brand.getName())
                .imageLink(brand.getImageLink())
                .build();
    }

    public BrandEntity convertDtoToBrandEntity(CreateOrUpdateBrandDto dto) {
        return BrandEntity.builder()
                .imageLink(dto.getImageLink())
                .name(dto.getName())
                .build();
    }
}
