package com.school.kiqa.persistence.repository;

import com.school.kiqa.persistence.entity.OrderProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProductEntity, Long> {
}
