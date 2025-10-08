package com.im.inventory.controller;

import com.im.inventory.entity.Order;
import com.im.inventory.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        return orderService.placeOrder(userId, productId, quantity);
    }

    @PatchMapping("{id}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id) {
        return orderService.cancelOrder(id);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable Long userId) {
        return orderService.getUserOrders(userId);
    }

    @GetMapping("/admin/all")
    public ResponseEntity<List<Order>> getAllOrdersForAdmin() {
        return orderService.getAllOrdersForAdmin();
    }
}
