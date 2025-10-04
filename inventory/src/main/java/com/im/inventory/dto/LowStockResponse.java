package com.im.inventory.dto;

import com.im.inventory.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LowStockResponse {
    private int totalProducts;
    private List<Product> products;
}