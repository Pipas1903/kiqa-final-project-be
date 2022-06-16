package com.school.kiqa.persistence.specifications;

import com.school.kiqa.persistence.entity.ProductEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ProductSpecifications {

    public static Specification<ProductEntity> withCategory(List<Long> categoryIds) {
        if (categoryIds.isEmpty())
            return null;
        return ((root, query, criteriaBuilder) -> root.get("categoryEntity").in(categoryIds));
    }

    public static Specification<ProductEntity> withProductType(List<Long> productTypeIds) {
        if (productTypeIds.isEmpty())
            return null;
        return ((root, query, criteriaBuilder) -> root.get("productTypeEntity").in(productTypeIds));
    }

    public static Specification<ProductEntity> withBrand(List<Long> brandIds) {
        if (brandIds.isEmpty())
            return null;
        return (root, query, criteriaBuilder) -> root.get("brandEntity").in(brandIds);
    }

    public static Specification<ProductEntity> startingAtPrice(double minPrice) {
        if (minPrice == 0)
            return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<ProductEntity> endingAtPrice(double maxPrice) {
        if (maxPrice == 0)
            return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
    }
}
