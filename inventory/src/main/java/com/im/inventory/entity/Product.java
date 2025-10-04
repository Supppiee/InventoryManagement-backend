package com.im.inventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    private Long id;

    @NotBlank(message = "Product name is required")
    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @Min(value = 0, message = "Stock quantity cannot be negative")
    @Column(nullable = false)
    private int stockQuantity;

    @Min(value = 0, message = "Low stock threshold cannot be negative")
    @Column(nullable = false)
    private int lowStockThreshold = 10;
}
