package com.im.inventory.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OrderResponse {
    private String trackingId;
    private String productName;
    private String username;
    private int quantity;
    private String status;
    private String orderedBy;
    private double bill;
    private LocalDateTime createdAt;
}