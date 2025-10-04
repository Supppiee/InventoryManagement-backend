package com.im.inventory.service;

import com.im.inventory.entity.Product;
import com.im.inventory.entity.Stock;
import com.im.inventory.repository.AuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditService {

    @Autowired
    private AuditRepository auditRepository;

    public void logStockTransaction(Product product, int quantity, String type) {
        Stock stockLog = new Stock();
        stockLog.setProduct(product);
        stockLog.setQuantityChange(quantity);
        stockLog.setType(type);
        stockLog.setTimestamp(LocalDateTime.now());

        auditRepository.save(stockLog);
    }

    public Page<Stock> getAllTransactions(int page, int size, String sortBy, String sortDir) {
        Sort sort = "desc".equalsIgnoreCase(sortDir) ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return auditRepository.findAll(pageable);
    }

    public Page<Stock> getTransactionsByProduct(Long productId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return auditRepository.findByProductId(productId, pageable);
    }

    public Page<Stock> getTransactionsByType(String type, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return auditRepository.findByType(type, pageable);
    }
}