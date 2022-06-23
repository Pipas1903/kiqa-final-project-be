package com.school.kiqa.service;

import com.school.kiqa.command.Paginated;
import com.school.kiqa.command.dto.product.CreateOrUpdateProductDto;
import com.school.kiqa.command.dto.product.ProductDetailsDto;
import com.school.kiqa.converter.ProductConverter;
import com.school.kiqa.exception.notFound.BrandNotFoundException;
import com.school.kiqa.exception.notFound.CategoryNotFoundException;
import com.school.kiqa.exception.notFound.ProductNotFoundException;
import com.school.kiqa.exception.notFound.ProductTypeNotFoundException;
import com.school.kiqa.exception.notFound.ResultsNotFoundException;
import com.school.kiqa.persistence.entity.CategoryEntity;
import com.school.kiqa.persistence.entity.ProductEntity;
import com.school.kiqa.persistence.repository.BrandRepository;
import com.school.kiqa.persistence.repository.CategoryRepository;
import com.school.kiqa.persistence.repository.ProductRepository;
import com.school.kiqa.persistence.repository.ProductTypeRepository;
import com.school.kiqa.persistence.specifications.ProductSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


import static com.school.kiqa.exception.ErrorMessageConstants.*;
import static com.school.kiqa.persistence.specifications.ProductSpecifications.*;

import static com.school.kiqa.exception.ErrorMessageConstants.BRAND_NOT_FOUND_BY_NAME;
import static com.school.kiqa.exception.ErrorMessageConstants.CATEGORY_NOT_FOUND_BY_NAME;
import static com.school.kiqa.exception.ErrorMessageConstants.NO_RESULTS_FOUND;
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

        if (products.getContent().isEmpty()) {
            log.error("Couldn't find products with brands - {}, categories - {}, productTypes - {}, minPrice - {}, maxPrice - {}",
                    brands, categories, productTypes, minPrice, maxPrice);
            throw new ResultsNotFoundException(NO_RESULTS_FOUND);
        }

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

        //TODO: set product brand, category, productType etc

        ProductEntity product = converter.convertDtoToProductEntity(createOrUpdateProductDto);

        product.setId(productEntity.getId());

        log.info("product with id {} was successfully updated", id);
        return converter.convertEntityToProductDetailsDto(productRepository.save(product));
    }

    @Override
    public ProductDetailsDto activateOrDeactivateProduct(Long id, boolean activateProduct) {
    
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn(String.format(PRODUCT_NOT_FOUND, id));
                    return new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND, id));
                });

        return activateProduct ? activateProduct(productEntity, id) : deactivateProduct(productEntity, id);
    }

    @Override
    public ProductDetailsDto activateProduct(ProductEntity productEntity, Long id) {
        productEntity.setIsActive(true);
        final var savedItem = productRepository.save(productEntity);
        log.info("product with id {} was successfully activated", id);
        return converter.convertEntityToProductDetailsDto(savedItem);
    }

    @Override
    public ProductDetailsDto deactivateProduct(ProductEntity productEntity, Long id) {
        productEntity.setIsActive(false);
        final var savedItem = productRepository.save(productEntity);
        log.info("product with id {} was successfully deactivated", id);
        return converter.convertEntityToProductDetailsDto(savedItem);
    }
    
    public Paginated<ProductDetailsDto> searchProductsByName(String name, PageRequest pageRequest) {

        Page<ProductEntity> products = productRepository.searchAllByNameContainingIgnoreCase(name, pageRequest);

        if (products.isEmpty()) {
            log.error("Couldn't find products with '{}' in name", name);
            throw new ResultsNotFoundException(NO_RESULTS_FOUND);
        }

        log.info("Retrieved {} results from search by name '{}'", products.getTotalElements(), name);

        final List<ProductDetailsDto> list = products.stream()
                .map(converter::convertEntityToProductDetailsDto)
                .collect(Collectors.toList());

        Paginated<ProductDetailsDto> paginated = new Paginated<>(
                list,
                products.getNumberOfElements(),
                products.getPageable().getPageNumber(),
                products.getTotalPages(),
                products.getTotalElements());

        log.info("Returned products containing {} in the name", name);
        return paginated;
    }
    
    public List<ProductDetailsDto> getRelatedProducts(String categoryName) {

        CategoryEntity category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> {
                    log.warn(String.format(CATEGORY_NOT_FOUND_BY_NAME, categoryName));
                    return new CategoryNotFoundException(String.format(CATEGORY_NOT_FOUND_BY_NAME, categoryName));
                });

        long numberOfProducts = 10;

        //create specification to get products with given category
        Specification<ProductEntity> specification = Specification.where(ProductSpecifications
                .withCategory(List.of(category.getId())));

        // total number of products with given category
        long count = productRepository.count(specification);
        log.info("There are {} products with category '{}'", count, categoryName);

        Random random = new Random();
        long numberOfPages = count / numberOfProducts;
        log.info("For {} results there are {} pages", numberOfProducts, numberOfPages);

        int randomFirstPage = random.nextInt((int) numberOfPages);
        log.info("Generated random to get first page: {}", randomFirstPage);

        final PageRequest pageRequest = PageRequest.of(randomFirstPage, (int) numberOfProducts);

        Page<ProductEntity> relatedProducts = productRepository.findAll(specification, pageRequest);
        log.info("Retrieved {} products from database", relatedProducts.getPageable().getPageSize());

        List<ProductDetailsDto> convertedProducts = relatedProducts.getContent().stream()
                .map(converter::convertEntityToProductDetailsDto)
                .collect(Collectors.toList());

        log.info("Converted productEntities to productDetailsDto");

        return convertedProducts;
    }
}
