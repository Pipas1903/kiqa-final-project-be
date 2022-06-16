package com.school.kiqa.service;

import com.school.kiqa.command.Paginated;
import com.school.kiqa.command.dto.CreateOrUpdateProductDto;
import com.school.kiqa.command.dto.ProductDetailsDto;
import com.school.kiqa.converter.ColorConverter;
import com.school.kiqa.converter.ProductConverter;
import com.school.kiqa.exception.BrandNotFoundException;
import com.school.kiqa.exception.CategoryNotFoundException;
import com.school.kiqa.exception.ProductNotFoundException;
import com.school.kiqa.exception.ProductTypeNotFoundException;
import com.school.kiqa.persistence.entity.BrandEntity;
import com.school.kiqa.persistence.entity.ProductEntity;
import com.school.kiqa.persistence.repository.BrandRepository;
import com.school.kiqa.persistence.repository.CategoryRepository;
import com.school.kiqa.persistence.repository.ColorRepository;
import com.school.kiqa.persistence.repository.ProductRepository;
import com.school.kiqa.persistence.repository.ProductTypeRepository;
import com.school.kiqa.persistence.specifications.ProductSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.school.kiqa.exception.ErrorMessageConstants.BRAND_NOT_FOUND_BY_NAME;
import static com.school.kiqa.exception.ErrorMessageConstants.CATEGORY_NOT_FOUND_BY_NAME;
import static com.school.kiqa.exception.ErrorMessageConstants.PRODUCT_NOT_FOUND;
import static com.school.kiqa.exception.ErrorMessageConstants.PRODUCT_TYPE_NOT_FOUND_BY_NAME;
import static com.school.kiqa.persistence.specifications.ProductSpecifications.endingAtPrice;
import static com.school.kiqa.persistence.specifications.ProductSpecifications.startingAtPrice;
import static com.school.kiqa.persistence.specifications.ProductSpecifications.withBrand;
import static com.school.kiqa.persistence.specifications.ProductSpecifications.withCategory;
import static com.school.kiqa.persistence.specifications.ProductSpecifications.withProductType;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductTypeRepository productTypeRepository;
    private final ProductConverter converter;

    @Override
    public Paginated<ProductDetailsDto> getAllProducts(
            PageRequest pageRequest,
            List<String> brands,
            List<String> productTypes,
            List<String> categories,
            double minPrice,
            double maxPrice
    ) {
        List<Long> brandId = new ArrayList<>();
        List<Long> categoryId = new ArrayList<>();
        List<Long> productTypeId = new ArrayList<>();

        if (brands != null) {
            brandId.addAll(brands.stream()
                    .map(brandName -> brandRepository.findByName(brandName)
                            .orElseThrow(() -> {
                                log.error(String.format(BRAND_NOT_FOUND_BY_NAME, brandName));
                                return new BrandNotFoundException(String.format(BRAND_NOT_FOUND_BY_NAME, brandName));
                            })
                            .getId()
                    )
                    .collect(Collectors.toList()));
            log.info("Fetched all brands to filter products");
        }

        if (categories != null) {
            categoryId.addAll(categories.stream()
                    .map(categoryName -> categoryRepository.findByName(categoryName)
                            .orElseThrow(() -> {
                                log.error(String.format(CATEGORY_NOT_FOUND_BY_NAME, categoryName));
                                return new CategoryNotFoundException(String.format(CATEGORY_NOT_FOUND_BY_NAME, categoryName));
                            })
                            .getId()
                    )
                    .collect(Collectors.toList()));
            log.info("Fetched all categories to filter products");
        }

        if (productTypes != null) {
            productTypeId.addAll(productTypes.stream()
                    .map(productTypeName -> productTypeRepository.findByName(productTypeName)
                            .orElseThrow(() -> {
                                log.error(String.format(PRODUCT_TYPE_NOT_FOUND_BY_NAME, productTypeName));
                                return new ProductTypeNotFoundException(String.format(PRODUCT_TYPE_NOT_FOUND_BY_NAME, productTypeName));
                            })
                            .getId()
                    )
                    .collect(Collectors.toList()));
            log.info("Fetched all products to filter products");
        }

        final Page<ProductEntity> products = productRepository
                .findAll(Specification
                                .where(withBrand(brandId))
                                .and(withProductType(productTypeId))
                                .and(withCategory(categoryId))
                                .and(startingAtPrice(minPrice))
                                .and(endingAtPrice(maxPrice)),
                        pageRequest);

        log.info("Filtered products");

        final List<ProductDetailsDto> list = products.stream()
                .map(converter::convertEntityToProductDetailsDto)
                .collect(Collectors.toList());

        Paginated<ProductDetailsDto> paginated = new Paginated<>(
                list,
                products.getNumberOfElements(),
                products.getPageable().getPageNumber(),
                products.getTotalPages(),
                products.getTotalElements());

        log.info("Returned all products successfully");
        return paginated;
    }

    @Override
    public ProductDetailsDto getProductById(Long id) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("product with id {} does not exist", id);
                    return new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND, id));
                });


        log.info("returned product with id {} successfully", id);
        return converter.convertEntityToProductDetailsDto(productEntity);
    }

    @Override
    public ProductDetailsDto updateProductById(Long id, CreateOrUpdateProductDto createOrUpdateProductDto) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("product with id {} does not exist", id);
                    return new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND, id));
                });

        ProductEntity product = converter.convertDtoToProductEntity(createOrUpdateProductDto);

        product.setId(productEntity.getId());

        log.info("product with id {} was successfully updated", id);
        return converter.convertEntityToProductDetailsDto(productRepository.save(product));
    }

    @Override
    public ProductDetailsDto deactivateProduct(Long id) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("product with id {} does not exist", id);
                    return new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND, id));
                });

        productEntity.setIsActive(false);
        final var savedItem = productRepository.save(productEntity);
        log.info("product with id {} was successfully deactivated", id);
        return converter.convertEntityToProductDetailsDto(savedItem);
    }

    @Override
    public ProductDetailsDto activateProduct(Long id) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("product with id {} does not exist", id);
                    return new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND, id));
                });

        productEntity.setIsActive(true);
        final var savedItem = productRepository.save(productEntity);
        log.info("product with id {} was successfully activated", id);
        return converter.convertEntityToProductDetailsDto(savedItem);
    }
}