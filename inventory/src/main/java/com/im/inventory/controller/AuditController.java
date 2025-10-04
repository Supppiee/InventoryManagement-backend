package com.im.inventory.controller;

import com.im.inventory.entity.Stock;
import com.im.inventory.service.AuditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Audit Management", description = "APIs for tracking stock quantity changes and transactions")
@RestController
@RequestMapping("api/auditQuantity")
public class AuditController {

    @Autowired
    private AuditService auditService;

    @Operation(summary = "Get all stock transactions", description = "Retrieves all stock change records with pagination and sorting options")
    @GetMapping
    public ResponseEntity<Page<Stock>> getAllTransactions(
            @Parameter(description = "Page number (default: 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size (default: 10)") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field (default: timestamp)") @RequestParam(defaultValue = "timestamp") String sortBy,
            @Parameter(description = "Sort direction (asc or desc, default: desc)") @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Page<Stock> transactions = auditService.getAllTransactions(page, size, sortBy, sortDir);
        return transactions.hasContent() ?
                new ResponseEntity<>(transactions, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Get transactions by product", description = "Retrieves all stock change records for a specific product ID")
    @GetMapping("/product/{productId}")
    public ResponseEntity<Page<Stock>> getTransactionsByProduct(
            @Parameter(description = "Product ID to fetch transactions for") @PathVariable Long productId,
            @Parameter(description = "Page number (default: 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size (default: 10)") @RequestParam(defaultValue = "10") int size
    ) {
        Page<Stock> transactions = auditService.getTransactionsByProduct(productId, page, size);
        return transactions.hasContent() ?
                new ResponseEntity<>(transactions, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
