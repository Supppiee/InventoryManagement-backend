package com.im.inventory.dto;

import com.im.inventory.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PaginatedProductResponse {
    private List<Product> products;
    private int currentPage;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}