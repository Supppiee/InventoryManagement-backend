package com.im.inventory.repository;

import com.im.inventory.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT * FROM products WHERE stock_quantity < low_stock_threshold", nativeQuery = true)
    List<Product> findLowStockProducts();

}
