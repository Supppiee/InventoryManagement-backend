package com.im.inventory.repository;

import com.im.inventory.entity.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepository extends JpaRepository<Stock, Long> {
    Page<Stock> findByProductId(Long productId, Pageable pageable);
    Page<Stock> findByType(String type, Pageable pageable);
}