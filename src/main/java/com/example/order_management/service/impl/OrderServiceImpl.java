package com.example.order_management.service.impl;

import com.example.notification.OrderNotificationResponse;
import com.example.order_management.grpc.NotificationGrpcClient;
import com.example.order_management.model.Order;
import com.example.order_management.model.OrderItem;
import com.example.order_management.repository.ClientRepository;
import com.example.order_management.repository.OrderRepository;
import com.example.order_management.repository.ProductRepository;
import com.example.order_management.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final NotificationGrpcClient notificationClient;

    @Override
    public Order createOrder(Order order) {
        log.info("Creating new order for client ID: {}", order.getClient().getId());

        // Validate client exists
        if (order.getClient() == null || order.getClient().getId() == null) {
            throw new IllegalArgumentException("Client must be specified");
        }

        clientRepository.findById(order.getClient().getId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        // Set order date and initial status
        order.setOrderDate(LocalDateTime.now());
        if (order.getStatus() == null) {
            order.setStatus(Order.OrderStatus.PENDING);
        }

        // Validate and process order items
        for (OrderItem item : order.getItems()) {
            item.setOrder(order);

            // Validate product exists and set current price
            var product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + item.getProduct().getId()));

            item.setProduct(product);
            item.setPrice(product.getPrice());

            // Validate stock
            if (product.getStockQuantity() < item.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
            }

            // Update stock
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);
        }

        // Calculate total
        order.calculateTotal();

        Order savedOrder = orderRepository.save(order);
        log.info("Order created successfully with ID: {}", savedOrder.getId());

        // Send notification via gRPC
        try {
            OrderNotificationResponse notificationResponse = notificationClient.sendOrderNotification(
                    savedOrder.getId(),
                    savedOrder.getClient().getId(),
                    savedOrder.getClient().getName(),
                    savedOrder.getClient().getEmail(),
                    savedOrder.getTotalAmount().doubleValue(),
                    savedOrder.getStatus().name(),
                    String.format("Order #%d has been created with total amount $%.2f",
                            savedOrder.getId(), savedOrder.getTotalAmount())
            );

            if (notificationResponse.getSuccess()) {
                log.info("Notification sent successfully: {}", notificationResponse.getMessage());
            } else {
                log.warn("Notification failed: {}", notificationResponse.getMessage());
            }
        } catch (Exception e) {
            log.error("Error sending notification via gRPC", e);
            // Don't fail the order creation if notification fails
        }

        return savedOrder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrdersByClientId(Long clientId) {
        log.info("Fetching orders for client ID: {}", clientId);
        return orderRepository.findByClientId(clientId);
    }

    @Override
    @Transactional(readOnly = true)
    public Order getOrderById(Long orderId) {
        log.info("Fetching order with ID: {}", orderId);
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateOrderTotal(Long orderId) {
        Order order = getOrderById(orderId);
        return order.getTotalAmount();
    }

    @Override
    public Order updateOrderStatus(Long orderId, Order.OrderStatus newStatus) {
        log.info("Updating order {} status to {}", orderId, newStatus);

        Order order = getOrderById(orderId);

        // Validate status transition
        if (order.getStatus() == Order.OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cannot update status of a cancelled order");
        }

        if (order.getStatus() == Order.OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot update status of a delivered order");
        }

        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        log.info("Fetching all orders");
        return orderRepository.findAll();
    }

    @Override
    public Order cancelOrder(Long orderId) {
        log.info("Cancelling order with ID: {}", orderId);

        Order order = getOrderById(orderId);

        if (order.getStatus() == Order.OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot cancel a delivered order");
        }

        if (order.getStatus() == Order.OrderStatus.CANCELLED) {
            throw new IllegalStateException("Order is already cancelled");
        }

        // Restore stock for cancelled orders
        for (OrderItem item : order.getItems()) {
            var product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepository.save(product);
        }

        order.setStatus(Order.OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }
}

