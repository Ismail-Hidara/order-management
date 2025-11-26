package com.example.order_management.service;

import com.example.order_management.model.Order;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {

    /**
     * Create a new order
     */
    Order createOrder(Order order);

    /**
     * Get orders by client ID
     */
    List<Order> getOrdersByClientId(Long clientId);

    /**
     * Get order by ID
     */
    Order getOrderById(Long orderId);

    /**
     * Calculate total amount for an order
     */
    BigDecimal calculateOrderTotal(Long orderId);

    /**
     * Update order status
     */
    Order updateOrderStatus(Long orderId, Order.OrderStatus newStatus);

    /**
     * Get all orders
     */
    List<Order> getAllOrders();

    /**
     * Cancel an order
     */
    Order cancelOrder(Long orderId);
}


