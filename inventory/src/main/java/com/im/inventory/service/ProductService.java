package com.im.inventory.service;

import com.im.inventory.constants.Constant;
import com.im.inventory.dto.LowStockResponse;
import com.im.inventory.dto.PaginatedProductResponse;
import com.im.inventory.entity.Product;
import com.im.inventory.exceptions.DuplicateProductException;
import com.im.inventory.exceptions.InvalidStockOperationException;
import com.im.inventory.exceptions.ProductNotFoundException;
import com.im.inventory.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AuditService auditService;

    public ResponseEntity<Product> saveProduct(Product product) {
        if (productRepository.existsById(product.getId())) {
            throw new DuplicateProductException("Product with ID '" + product.getId() + "' already exists");
        }
        Product savedProduct = productRepository.save(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    public ResponseEntity<Product> getProduct(Long id) {
        Optional<Product> products = productRepository.findById(id);
        if(products.isEmpty()){
            throw new ProductNotFoundException(id);
        }
        return new ResponseEntity<>(products.get(), HttpStatus.FOUND);
    }

    public ResponseEntity<PaginatedProductResponse> getAllProducts(int page, int size, String sortBy, String sortDir) {
        Sort sort = "desc".equalsIgnoreCase(sortDir) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> productPage = productRepository.findAll(pageable);

        if (productPage.hasContent()) {
            PaginatedProductResponse response = new PaginatedProductResponse(
                    productPage.getContent(),
                    productPage.getNumber(),
                    productPage.getSize(),
                    productPage.getTotalElements(),
                    productPage.getTotalPages(),
                    productPage.isLast()
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        existing.setName(updatedProduct.getName());
        existing.setDescription(updatedProduct.getDescription());
        existing.setLowStockThreshold(updatedProduct.getLowStockThreshold());
        existing.setStockQuantity(updatedProduct.getStockQuantity());

        return productRepository.save(existing);
    }

    public ResponseEntity<?> deleteProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isEmpty()){
            throw new ProductNotFoundException(id);
        }
        productRepository.delete(product.get());
        return new ResponseEntity<>("Deleted product", HttpStatus.OK);
    }

    public Product increaseProductStock(Long id, int quantity) {
        if (quantity <= 0) {
            throw new InvalidStockOperationException("Quantity must be greater than zero");
        }
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        product.setStockQuantity(product.getStockQuantity() + quantity);
        Product savedProduct =  productRepository.save(product);
        auditService.logStockTransaction(savedProduct, quantity, Constant.INCREASE);
        return savedProduct;
    }

    public Product decreaseProductStock(Long id, int quantity) {
        if (quantity <= 0) {
            throw new InvalidStockOperationException("Quantity must be greater than zero");
        }
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        if (product.getStockQuantity() < quantity) {
            throw new InvalidStockOperationException(
                    "Insufficient stock: current=" + product.getStockQuantity() + ", requested=" + quantity
            );
        }
        product.setStockQuantity(product.getStockQuantity() - quantity);
        Product savedProduct = productRepository.save(product);
        auditService.logStockTransaction(savedProduct, quantity, Constant.DECREASE);
        return savedProduct;
    }

    public ResponseEntity<LowStockResponse> getLowStockProducts() {
        List<Product> lowStockProducts = productRepository.findLowStockProducts();
        if (lowStockProducts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        LowStockResponse response = new LowStockResponse(
                lowStockProducts.size(),
                lowStockProducts
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
