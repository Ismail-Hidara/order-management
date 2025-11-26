package com.example.order_management.web.graphql;

import com.example.order_management.model.Client;
import com.example.order_management.model.Order;
import com.example.order_management.model.OrderItem;
import com.example.order_management.model.Product;
import com.example.order_management.repository.ClientRepository;
import com.example.order_management.repository.ProductRepository;
import com.example.order_management.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class OrderGraphQLController {

    private final OrderService orderService;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;

    @QueryMapping
    public List<Order> orders() {
        return orderService.getAllOrders();
    }

    @QueryMapping
    public Order order(@Argument Long id) {
        return orderService.getOrderById(id);
    }

    @QueryMapping
    public List<Order> ordersByClient(@Argument Long clientId) {
        return orderService.getOrdersByClientId(clientId);
    }

    @QueryMapping
    public BigDecimal orderTotal(@Argument Long orderId) {
        return orderService.calculateOrderTotal(orderId);
    }

    @QueryMapping
    public List<Client> clients() {
        return clientRepository.findAll();
    }

    @QueryMapping
    public Client client(@Argument Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));
    }

    @QueryMapping
    public List<Product> products() {
        return productRepository.findAll();
    }

    @QueryMapping
    public Product product(@Argument Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }

    @MutationMapping
    public Order createOrder(@Argument Map<String, Object> input) {
        Long clientId = Long.valueOf(input.get("clientId").toString());
        String shippingAddress = (String) input.get("shippingAddress");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> itemsInput = (List<Map<String, Object>>) input.get("items");

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        Order order = new Order();
        order.setClient(client);
        order.setShippingAddress(shippingAddress);
        order.setStatus(Order.OrderStatus.PENDING);

        List<OrderItem> items = new ArrayList<>();
        for (Map<String, Object> itemInput : itemsInput) {
            Long productId = Long.valueOf(itemInput.get("productId").toString());
            Integer quantity = Integer.valueOf(itemInput.get("quantity").toString());

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(quantity);
            item.setPrice(product.getPrice());

            items.add(item);
        }

        order.setItems(items);
        return orderService.createOrder(order);
    }

    @MutationMapping
    public Order updateOrderStatus(@Argument Long orderId, @Argument String status) {
        Order.OrderStatus orderStatus = Order.OrderStatus.valueOf(status);
        return orderService.updateOrderStatus(orderId, orderStatus);
    }

    @MutationMapping
    public Order cancelOrder(@Argument Long orderId) {
        return orderService.cancelOrder(orderId);
    }
}

