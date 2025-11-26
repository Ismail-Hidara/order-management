package com.example.order_management.config;

import com.example.order_management.model.Client;
import com.example.order_management.model.Product;
import com.example.order_management.repository.ClientRepository;
import com.example.order_management.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        log.info("Initializing database with sample data...");

        // Create sample clients
        Client client1 = new Client();
        client1.setName("John Doe");
        client1.setEmail("john.doe@example.com");
        client1.setPhone("+1234567890");
        client1.setAddress("123 Main St, New York, NY 10001");
        clientRepository.save(client1);

        Client client2 = new Client();
        client2.setName("Jane Smith");
        client2.setEmail("jane.smith@example.com");
        client2.setPhone("+1987654321");
        client2.setAddress("456 Oak Ave, Los Angeles, CA 90001");
        clientRepository.save(client2);

        Client client3 = new Client();
        client3.setName("Bob Johnson");
        client3.setEmail("bob.johnson@example.com");
        client3.setPhone("+1555123456");
        client3.setAddress("789 Pine Rd, Chicago, IL 60601");
        clientRepository.save(client3);

        log.info("Created {} clients", clientRepository.count());

        // Create sample products
        Product product1 = new Product();
        product1.setName("Laptop Pro 15");
        product1.setDescription("High-performance laptop with 15-inch display");
        product1.setPrice(new BigDecimal("1299.99"));
        product1.setStockQuantity(50);
        product1.setCategory("Electronics");
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setName("Wireless Mouse");
        product2.setDescription("Ergonomic wireless mouse with USB receiver");
        product2.setPrice(new BigDecimal("29.99"));
        product2.setStockQuantity(200);
        product2.setCategory("Electronics");
        productRepository.save(product2);

        Product product3 = new Product();
        product3.setName("USB-C Hub");
        product3.setDescription("7-in-1 USB-C hub with HDMI and card reader");
        product3.setPrice(new BigDecimal("49.99"));
        product3.setStockQuantity(150);
        product3.setCategory("Accessories");
        productRepository.save(product3);

        Product product4 = new Product();
        product4.setName("Mechanical Keyboard");
        product4.setDescription("RGB mechanical gaming keyboard");
        product4.setPrice(new BigDecimal("89.99"));
        product4.setStockQuantity(75);
        product4.setCategory("Electronics");
        productRepository.save(product4);

        Product product5 = new Product();
        product5.setName("4K Monitor");
        product5.setDescription("27-inch 4K UHD monitor");
        product5.setPrice(new BigDecimal("399.99"));
        product5.setStockQuantity(30);
        product5.setCategory("Electronics");
        productRepository.save(product5);

        log.info("Created {} products", productRepository.count());
        log.info("Database initialization completed successfully!");
    }
}

