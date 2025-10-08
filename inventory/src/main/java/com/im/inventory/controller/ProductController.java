package com.im.inventory.controller;

import com.im.inventory.dto.responses.LowStockResponse;
import com.im.inventory.dto.responses.PaginatedProductResponse;
import com.im.inventory.entity.Product;
import com.im.inventory.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Product Management", description = "APIs for managing inventory products")
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Operation(summary = "Create a new product", description = "Adds a new product to the inventory")
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @Operation(summary = "Get product by ID", description = "Retrieves a specific product by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(
            @Parameter(description = "Product ID to fetch") @PathVariable Long id) {
        return productService.getProduct(id);
    }

    @Operation(summary = "Get all products (paginated)", description = "Retrieves all products with pagination and sorting options")
    @GetMapping("/all")
    public ResponseEntity<PaginatedProductResponse> getAllProducts(
            @Parameter(description = "Page number (default: 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size (default: 10)") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field (default: id)") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction (asc or desc, default: asc)") @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return productService.getAllProducts(page, size, sortBy, sortDir);
    }

    @Operation(summary = "Update product", description = "Updates details of an existing product")
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @Parameter(description = "Product ID to update") @PathVariable Long id,
            @RequestBody Product product) {
        Product updated = productService.updateProduct(id, product);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @Operation(summary = "Delete product", description = "Deletes a product from the inventory by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(
            @Parameter(description = "Product ID to delete") @PathVariable Long id) {
        return productService.deleteProduct(id);
    }

    @Operation(summary = "Increase product stock", description = "Increases the stock quantity of a product")
    @PatchMapping("/{id}/increaseStock")
    public ResponseEntity<Product> increaseStock(
            @Parameter(description = "Product ID to update") @PathVariable Long id,
            @Parameter(description = "Quantity to increase") @RequestParam int quantity) {
        Product updated = productService.increaseProductStock(id, quantity);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @Operation(summary = "Decrease product stock", description = "Decreases the stock quantity of a product")
    @PatchMapping("/{id}/decreaseStock")
    public ResponseEntity<Product> decreaseStock(
            @Parameter(description = "Product ID to update") @PathVariable Long id,
            @Parameter(description = "Quantity to decrease") @RequestParam int quantity) {
        Product updated = productService.decreaseProductStock(id, quantity);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @Operation(summary = "Get low stock products", description = "Fetches all products with low stock levels")
    @GetMapping("/lowStock")
    public ResponseEntity<LowStockResponse> getLowStockProducts() {
        return productService.getLowStockProducts();
    }
}
