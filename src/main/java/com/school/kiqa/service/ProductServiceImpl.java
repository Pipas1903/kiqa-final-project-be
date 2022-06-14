package com.school.kiqa.service;

import com.school.kiqa.command.Paginated;
import com.school.kiqa.command.dto.CreateOrUpdateProductDto;
import com.school.kiqa.command.dto.ProductDetailsDto;
import com.school.kiqa.converter.ColorConverter;
import com.school.kiqa.converter.ProductConverter;
import com.school.kiqa.exception.ProductNotFoundException;
import com.school.kiqa.persistence.entity.ProductEntity;
import com.school.kiqa.persistence.repository.ColorRepository;
import com.school.kiqa.persistence.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.school.kiqa.exception.ErrorMessageConstants.PRODUCT_NOT_FOUND;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductConverter converter;
    private final ColorRepository colorRepository;
    private final ColorConverter colorConverter;

    @Override
    public Paginated<ProductDetailsDto> getAllProducts(PageRequest pageRequest) {
        final Page<ProductEntity> products = productRepository.findAll(pageRequest);

        final List<ProductDetailsDto> list = products.stream()
                .map(converter::convertEntityToProductDetailsDto)
                .collect(Collectors.toList());

        Paginated<ProductDetailsDto> paginated = new Paginated<>(
                list,
                products.getNumberOfElements(),
                products.getPageable().getPageNumber(),
                products.getTotalPages(),
                products.getTotalElements());

        log.info("returned all products successfully");
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
    public Paginated<ProductDetailsDto> getProductsByBrand(String brand, PageRequest pageRequest) {

        final Page<ProductEntity> products = productRepository.findProductEntitiesByBrandEntityName(brand, pageRequest);

        final List<ProductDetailsDto> list = products.stream()
                .map(converter::convertEntityToProductDetailsDto)
                .collect(Collectors.toList());

        Paginated<ProductDetailsDto> paginated = new Paginated<>(
                list,
                products.getNumberOfElements(),
                products.getPageable().getPageNumber(),
                products.getTotalPages(),
                products.getTotalElements());

        log.info("returned all products successfully");

        return paginated;
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
}