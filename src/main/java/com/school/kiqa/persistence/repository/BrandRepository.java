package com.school.kiqa.persistence.repository;

import com.school.kiqa.persistence.entity.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<BrandEntity, Long> {

    Optional<BrandEntity> findByName(String name);


    @Query(
            value = "select distinct brand.name from brand " +
                    "join product on (product.brand_id = brand.brand_id)",
            nativeQuery = true
    )
    List<String> findUsedBrands();
}
