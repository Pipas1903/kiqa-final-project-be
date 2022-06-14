package com.school.kiqa.persistence.repository;

import com.school.kiqa.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    @Query(
            value = "select case when exists (select * from category\n" +
                    "where upper(\"name\") = upper(:categoryName)) \n" +
                    "then 'true' else 'false' end;",
            nativeQuery = true
    )
    Boolean isCategoryNameUnavailable(@Param(value = "categoryName") String categoryName);

    Optional<CategoryEntity> findByName(String name);
}
