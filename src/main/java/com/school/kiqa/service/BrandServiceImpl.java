package com.school.kiqa.service;

import com.school.kiqa.command.Paginated;
import com.school.kiqa.command.dto.BrandDetailsDto;
import com.school.kiqa.command.dto.CreateOrUpdateBrandDto;
import com.school.kiqa.converter.BrandConverter;
import com.school.kiqa.exception.AlreadyExistsException;
import com.school.kiqa.exception.BrandNotFoundException;
import com.school.kiqa.exception.ErrorMessageConstants;
import com.school.kiqa.persistence.entity.BrandEntity;
import com.school.kiqa.persistence.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.school.kiqa.exception.ErrorMessageConstants.BRAND_NOT_FOUND_BY_NAME;

@Service
@Slf4j
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandConverter converter;

    @Override
    public BrandDetailsDto createBrand(CreateOrUpdateBrandDto brandDto) {
        brandRepository.findByName(brandDto.getName())
                .ifPresent(product -> {
                    log.warn(String.format(
                            ErrorMessageConstants.BRAND_ALREADY_EXISTS,
                            brandDto.getName()));

                    throw new AlreadyExistsException(String.format(
                            ErrorMessageConstants.BRAND_ALREADY_EXISTS,
                            brandDto.getName()));
                });

        BrandEntity savedEntity = brandRepository.save(converter.convertDtoToBrandEntity(brandDto));
        log.info("New brand named {} saved to database", savedEntity.getName());

        return converter.convertEntityToBrandDto(savedEntity);
    }

    @Override
    public Paginated<BrandDetailsDto> getAllBrands(PageRequest pageRequest) {

        Page<BrandEntity> brandEntities = brandRepository.findAll(pageRequest);

        final List<BrandDetailsDto> list = brandEntities.stream()
                .map(converter::convertEntityToBrandDto)
                .collect(Collectors.toList());

        Paginated<BrandDetailsDto> paginated = new Paginated<>(
                list,
                brandEntities.getNumberOfElements(),
                brandEntities.getPageable().getPageNumber(),
                brandEntities.getTotalPages(),
                brandEntities.getTotalElements());

        log.info("returned all brands successfully");
        return paginated;
    }

    @Override
    public List<String> getUsedBrandNames() {
        List<String> brandNames = brandRepository.findUsedBrands();
        log.info("returned all brand names successfully");
        return brandNames;
    }

    @Override
    public BrandDetailsDto getBrandByName(String name) {
        BrandEntity brand = brandRepository.findByName(name)
                .orElseThrow(() -> {
                    log.warn(String.format(BRAND_NOT_FOUND_BY_NAME, name));
                    return new BrandNotFoundException(String.format(BRAND_NOT_FOUND_BY_NAME, name));
                });

        log.info("Retrieved brand {} from database", brand.getName());

        return converter.convertEntityToBrandDto(brand);
    }
}