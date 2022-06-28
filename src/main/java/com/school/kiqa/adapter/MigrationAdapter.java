package com.school.kiqa.adapter;

import com.school.kiqa.command.dto.color.CreateOrUpdateColorDto;
import com.school.kiqa.exception.notFound.BrandNotFoundException;
import com.school.kiqa.exception.notFound.CategoryNotFoundException;
import com.school.kiqa.exception.notFound.ColorNotFoundException;
import com.school.kiqa.exception.notFound.ProductTypeNotFoundException;
import com.school.kiqa.persistence.entity.BrandEntity;
import com.school.kiqa.persistence.entity.CategoryEntity;
import com.school.kiqa.persistence.entity.ColorEntity;
import com.school.kiqa.persistence.entity.ProductEntity;
import com.school.kiqa.persistence.entity.ProductTypeEntity;
import com.school.kiqa.persistence.repository.BrandRepository;
import com.school.kiqa.persistence.repository.CategoryRepository;
import com.school.kiqa.persistence.repository.ColorRepository;
import com.school.kiqa.persistence.repository.ProductRepository;
import com.school.kiqa.persistence.repository.ProductTypeRepository;
import com.school.kiqa.service.ColorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.school.kiqa.exception.ErrorMessageConstants.BRAND_NOT_FOUND_BY_NAME;
import static com.school.kiqa.exception.ErrorMessageConstants.CATEGORY_NOT_FOUND_BY_NAME;
import static com.school.kiqa.exception.ErrorMessageConstants.COLOR_NOT_FOUND_BY_HEX_VALUE;
import static com.school.kiqa.exception.ErrorMessageConstants.PRODUCT_TYPE_NOT_FOUND_BY_NAME;

@Service
@Slf4j
@RequiredArgsConstructor
public class MigrationAdapter implements ProductApi {

    private final ColorService colorService;
    private final CategoryRepository categoryRepository;
    private final ColorRepository colorRepository;
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final ProductTypeRepository productTypeRepository;

    @Async
    @Override
    public void migrateApiToKiqaApi() {

        final var productList = getProductsFromExternalApi();

        List<ProductEntity> productEntityList = new ArrayList<>();

        productList.forEach(productFromApi -> {

            if (productFromApi.getPrice() == null || productFromApi.getPrice() == 0) {
                log.warn("product with no price, skipping...");
                return;
            }
            if (productFromApi.getBrand() == null) {
                log.warn("product with no brand, skipping...");
                return;
            }
            if (productFromApi.getDescription() == null) {
                log.warn("product with no description, skipping...");
                return;
            }

            saveProductColorsInDatabase(productFromApi);
            productEntityList.add(convertApiProductsToProductEntity(productFromApi));
        });

        productEntityList.forEach(product -> {
            log.info("trying to save product named " + product.getName());
            productRepository.save(product);
            log.info("product saved");
        });

        log.info("all products saved to database");
    }

    public ProductEntity convertApiProductsToProductEntity(ProductFromApi productFromApi) {
        ProductEntity product = ProductApiConverter.convertProductApiToProductEntity(productFromApi);
        log.info("Product basic information converted");

        CategoryEntity category = categoryRepository.findByName(productFromApi.getProduct_type().getCATEGORY())
                .orElseThrow(() -> {
                    log.warn(String.format(CATEGORY_NOT_FOUND_BY_NAME, productFromApi.getProduct_type().getCATEGORY()));
                    return new CategoryNotFoundException(String.format(CATEGORY_NOT_FOUND_BY_NAME, productFromApi.getProduct_type().getCATEGORY()));
                });

        product.setCategoryEntity(category);
        log.info("Product category set");

        BrandEntity brand = brandRepository.findByName(productFromApi.getBrand())
                .orElseThrow(() -> {
                    log.warn(String.format(BRAND_NOT_FOUND_BY_NAME, productFromApi.getBrand()));
                    return new BrandNotFoundException(String.format(BRAND_NOT_FOUND_BY_NAME, productFromApi.getBrand()));
                });

        product.setBrandEntity(brand);
        log.info("Product brand set");

        ProductTypeEntity productType = productTypeRepository.findByName(productFromApi.getProduct_type().toString())
                .orElseThrow(() -> {
                    log.warn(String.format(PRODUCT_TYPE_NOT_FOUND_BY_NAME, productFromApi.getProduct_type().toString()));
                    return new ProductTypeNotFoundException(String.format(PRODUCT_TYPE_NOT_FOUND_BY_NAME, productFromApi.getProduct_type().toString()));
                });

        product.setProductTypeEntity(productType);
        log.info("Product type set");

        List<ColorEntity> colors = productFromApi.getProduct_colors()
                .stream()
                .filter(color -> !color.getHex_value().contains(",") && color.getHex_value().contains("#") && color.getColour_name() != null)
                .map(color -> colorRepository.findByHexValue(color.getHex_value().toUpperCase())
                        .orElseThrow(() -> {
                            log.warn(String.format(COLOR_NOT_FOUND_BY_HEX_VALUE, color.getHex_value()));
                            return new ColorNotFoundException(String.format(COLOR_NOT_FOUND_BY_HEX_VALUE, color.getHex_value()));
                        }))
                .collect(Collectors.toList());

        product.setColors(colors);
        log.info("Product colors set");

        return product;
    }

    /**
     * @param product This method iterates over product and if it's valid
     */
    public void saveProductColorsInDatabase(ProductFromApi product) {
        product.getProduct_colors().forEach(color -> {
            log.info(String.format("trying to save color %s to database", color.getHex_value()));

            if (color.getColour_name() == null) {
                log.warn("color with no name, skipping...");
                return;
            }

            if (color.getHex_value().contains(",") || !color.getHex_value().contains("#")) {
                log.warn("invalid color format, skipping...");
                return;
            }

            if (colorRepository.findByHexValue(color.getHex_value().toUpperCase()).isPresent()) {
                log.warn("color already exists, skipping...");
                return;
            }

            CreateOrUpdateColorDto colorDto = CreateOrUpdateColorDto.builder()
                    .colourName(color.getColour_name().toUpperCase())
                    .hexValue(color.getHex_value().toUpperCase())
                    .build();

            final var savedColor = colorService.createColor(colorDto);

            log.info("color saved " + savedColor.getHexValue());
        });
    }

    @Override
    public List<ProductFromApi> getProductsFromExternalApi() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://makeup-api.herokuapp.com/api/v1/products.json";

        final var response = restTemplate.getForEntity(url, ProductFromApi[].class);

        return Arrays.stream(Objects.requireNonNull(response.getBody()))
                .collect(Collectors.toList());
    }

    @Async
    @Override
    public void updateProductImage() {
        final var products = getProductsFromExternalApi();

        products.forEach(productFromApi -> {
            if (!productFromApi.getApi_featured_image().startsWith("https:")) {
                productFromApi.setApi_featured_image("https:" + productFromApi.getApi_featured_image());
                log.warn("Update api image to have 'https:' before image link");
            }

            Optional<ProductEntity> product = productRepository.findByName(productFromApi.getName());

            if (product.isEmpty()) {
                log.warn("Product with name {} not found", productFromApi.getName());
                return;
            }

            log.warn("Found product by name '{}' and with id {}", product.get().getName(), product.get().getId());

            product.get().setImage(productFromApi.getApi_featured_image());
            log.info("product image updated");

            productRepository.save(product.get());
            log.info("saved updated product to database");
        });
        log.info("Finished setting images");
    }
}
