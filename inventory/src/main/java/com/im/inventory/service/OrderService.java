package com.im.inventory.service;

import com.im.inventory.constants.Constant;
import com.im.inventory.dto.responses.OrderResponse;
import com.im.inventory.entity.Order;
import com.im.inventory.entity.Product;
import com.im.inventory.entity.User;
import com.im.inventory.exceptions.ProductNotFoundException;
import com.im.inventory.repository.OrderRepository;
import com.im.inventory.repository.ProductRepository;
import com.im.inventory.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditService auditService;

    private OrderResponse orderResponse(Order order){
        return new OrderResponse(
                order.getTrackingId(),
                order.getProduct().getName(),
                order.getUser().getUsername(),
                order.getQuantity(),
                order.getStatus(),
                order.getUser().getUsername(),
                order.getBill(),
                order.getCreatedAt()
        );
    }

    public ResponseEntity<?> placeOrder(Long userId, Long productId, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        if (product.getStockQuantity() < quantity) {
            return new ResponseEntity<>("Insufficient stock available", HttpStatus.BAD_REQUEST);
        }

        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);

        Order order = new Order();
        order.setTrackingId(UUID.randomUUID().toString());
        order.setUser(user);
        order.setBill(product.getPrice() * quantity);
        order.setProduct(product);
        order.setQuantity(quantity);
        order.setStatus(Constant.PLACE);
        order.setCreatedAt(LocalDateTime.now());
        orderRepository.save(order);

        String actor = user.getRole().equalsIgnoreCase("ADMIN") ? "ADMIN" : user.getUsername();
        auditService.logStockTransaction(product, quantity, Constant.ORDER, actor);

        OrderResponse response = orderResponse(order);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<?> cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.isDeleted()) {
            return new ResponseEntity<>("Order already cancelled", HttpStatus.BAD_REQUEST);
        }

        User actor = userRepository.findById(order.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = order.getProduct();
        product.setStockQuantity(product.getStockQuantity() + order.getQuantity());
        productRepository.save(product);

        order.setDeleted(true);
        order.setStatus(Constant.CANCEL);
        orderRepository.save(order);

        String actorName = actor.getRole().equalsIgnoreCase("ADMIN") ? "ADMIN" : actor.getUsername();
        auditService.logStockTransaction(product, order.getQuantity(),  Constant.CANCEL, actorName);

        return new ResponseEntity<>("Order has been successfully cancelled!", HttpStatus.OK);
    }

    public ResponseEntity<List<Order>> getUserOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    public ResponseEntity<List<Order>> getAllOrdersForAdmin() {
        List<Order> allOrders = orderRepository.findAll();
        return new ResponseEntity<>(allOrders, HttpStatus.OK);
    }

}


