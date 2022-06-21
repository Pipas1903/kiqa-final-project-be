package com.school.kiqa.persistence.repository;

import com.school.kiqa.persistence.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>, JpaSpecificationExecutor<ProductEntity> {

    Page<ProductEntity> findProductEntitiesByBrandEntityName(String name, Pageable pageable);

    Page<ProductEntity> searchAllByNameContainingIgnoreCase(String name, Pageable pageable);
}
