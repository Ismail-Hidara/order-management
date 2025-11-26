package com.example.order_management.repository;

import com.example.order_management.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RepositoryRestResource(path = "orders")
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByClientId(Long clientId);
    List<Order> findByStatus(Order.OrderStatus status);

    @Query("SELECT o FROM Order o JOIN FETCH o.items WHERE o.id = :id")
    Order findByIdWithItems(Long id);
}

