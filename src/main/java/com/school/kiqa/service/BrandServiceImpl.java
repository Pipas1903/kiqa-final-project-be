package com.school.kiqa.service;

import com.school.kiqa.command.Paginated;
import com.school.kiqa.command.dto.brand.BrandDetailsDto;
import com.school.kiqa.command.dto.brand.CreateOrUpdateBrandDto;
import com.school.kiqa.converter.BrandConverter;
import com.school.kiqa.exception.ErrorMessageConstants;
import com.school.kiqa.exception.alreadyExists.AlreadyExistsException;
import com.school.kiqa.exception.notFound.BrandNotFoundException;
import com.school.kiqa.persistence.entity.BrandEntity;
import com.school.kiqa.persistence.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.school.kiqa.exception.ErrorMessageConstants.BRAND_NOT_FOUND_BY_ID;
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

        return converter.convertEntityToBrandDetailsDto(savedEntity);
    }

    @Override
    public Paginated<BrandDetailsDto> getAllBrands(PageRequest pageRequest, boolean hasProducts) {

        Page<BrandEntity> brandEntities = hasProducts ?
                brandRepository.findDistinctByProductEntityListIsNotNullOrderByNameAsc(pageRequest) :
                brandRepository.findAll(pageRequest);

        final List<BrandDetailsDto> list = brandEntities.stream()
                .map(converter::convertEntityToBrandDetailsDto)
                .collect(Collectors.toList());


        Paginated<BrandDetailsDto> paginated = new Paginated<>(
                list,
                brandEntities.getNumberOfElements(),
                brandEntities.getPageable().getPageNumber(),
                brandEntities.getTotalPages(),
                brandEntities.getTotalElements());

        log.info("received all brands from database successfully");
        return paginated;
    }

    @Override
    public BrandDetailsDto getBrandByName(String name) {
        BrandEntity brand = brandRepository.findByName(name)
                .orElseThrow(() -> {
                    log.warn(String.format(BRAND_NOT_FOUND_BY_NAME, name));
                    throw new BrandNotFoundException(String.format(BRAND_NOT_FOUND_BY_NAME, name));
                });

        log.info("Retrieved brand {} from database", brand.getName());
        return converter.convertEntityToBrandDetailsDto(brand);
    }

    @Override
    public BrandDetailsDto getBrandById(Long brandId) {
        BrandEntity brand = brandRepository.findById(brandId)
                .orElseThrow(() -> {
                    log.warn(String.format(BRAND_NOT_FOUND_BY_ID, brandId));
                    throw new BrandNotFoundException(String.format(BRAND_NOT_FOUND_BY_ID, brandId));
                });

        log.info("Retrieved brand {} from database", brand.getId());
        return converter.convertEntityToBrandDetailsDto(brand);
    }

    @Override
    public BrandDetailsDto updateBrandById(Long brandId, CreateOrUpdateBrandDto createOrUpdateBrandDto) {
        BrandEntity brandEntity = brandRepository.findById(brandId)
                .orElseThrow(() -> {
                    log.warn("brand with id {} does not exist", brandId);
                    throw new BrandNotFoundException(String.format(BRAND_NOT_FOUND_BY_ID, brandId));
                });

        BrandEntity brand = converter.convertDtoToBrandEntity(createOrUpdateBrandDto);
        brand.setId(brandId);

        final var savedBrand = brandRepository.save(brand);
        log.info("Saved updated brand with id {} to database", savedBrand.getId());

        log.info("brand with id {} was successfully updated", brandId);
        return converter.convertEntityToBrandDetailsDto(brand);
    }
}
