package com.school.kiqa.persistence.repository;

import com.school.kiqa.persistence.entity.CategoryEntity;
import com.school.kiqa.persistence.entity.ColorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ColorRepository extends JpaRepository<ColorEntity, Long> {

    Optional<ColorEntity> findByHexValue(String hexValue);
    Optional<ColorEntity> findByColourName(String colourName);
}
