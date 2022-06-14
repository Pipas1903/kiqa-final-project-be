package com.school.kiqa.persistence.repository;

import com.school.kiqa.persistence.entity.ProductTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductTypeEntity, Long> {

    Optional<ProductTypeEntity> findByName(String name);
}
