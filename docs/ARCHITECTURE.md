# Architecture Documentation

## System Overview

The Order Management System is built using a **multi-layered architecture** with support for **multiple API protocols** (REST, SOAP, GraphQL, gRPC). This design allows different types of clients to interact with the same business logic through their preferred communication protocol.

## Architectural Patterns

### 1. Layered Architecture

```
┌─────────────────────────────────────────────────────┐
│              Presentation Layer                      │
│  ┌──────┐  ┌──────┐  ┌─────────┐  ┌──────┐        │
│  │ REST │  │ SOAP │  │ GraphQL │  │ gRPC │        │
│  └──────┘  └──────┘  └─────────┘  └──────┘        │
└────────────────────┬────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────┐
│              Business Logic Layer                    │
│         ┌─────────────────────────┐                 │
│         │   OrderService (IoC)    │                 │
│         │  - createOrder()        │                 │
│         │  - getOrdersByClient()  │                 │
│         │  - updateOrderStatus()  │                 │
│         │  - calculateTotal()     │                 │
│         └─────────────────────────┘                 │
└────────────────────┬────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────┐
│           Data Access Layer                          │
│  ┌──────────────────────────────────────┐           │
│  │   Spring Data JPA Repositories       │           │
│  │  - ClientRepository                  │           │
│  │  - ProductRepository                 │           │
│  │  - OrderRepository                   │           │
│  │  - OrderItemRepository               │           │
│  └──────────────────────────────────────┘           │
└────────────────────┬────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────┐
│              Database Layer                          │
│         H2 In-Memory Database                        │
│  ┌──────┐  ┌─────────┐  ┌───────┐  ┌────────┐     │
│  │Client│  │ Product │  │ Order │  │OrderItem│     │
│  └──────┘  └─────────┘  └───────┘  └────────┘     │
└─────────────────────────────────────────────────────┘
```

### 2. Inversion of Control (IoC)

The system uses **Spring Framework's IoC container** to manage dependencies:

```java
@Service
public class OrderServiceImpl implements OrderService {
    
    // Dependencies injected by Spring
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final NotificationGrpcClient notificationClient;
    
    // Constructor injection (recommended by Spring)
    public OrderServiceImpl(
        OrderRepository orderRepository,
        ClientRepository clientRepository,
        ProductRepository productRepository,
        NotificationGrpcClient notificationClient
    ) {
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.notificationClient = notificationClient;
    }
}
```

**Benefits:**
- Loose coupling between components
- Easy to test (can inject mocks)
- Flexible configuration
- Single Responsibility Principle

### 3. Repository Pattern

Using **Spring Data JPA** for data access abstraction:

```java
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByClientId(Long clientId);
    List<Order> findByStatus(Order.OrderStatus status);
}
```

**Benefits:**
- Abstraction over data access
- Automatic CRUD implementations
- Query derivation from method names
- Type-safe queries

### 4. Service Layer Pattern

Business logic encapsulated in service interfaces and implementations:

```java
public interface OrderService {
    Order createOrder(Order order);
    List<Order> getOrdersByClientId(Long clientId);
    Order updateOrderStatus(Long orderId, OrderStatus status);
    // ... other business methods
}
```

**Benefits:**
- Clear separation of concerns
- Reusable business logic across multiple controllers
- Transaction management
- Easy to add cross-cutting concerns

## Component Diagram

```
┌──────────────────────────────────────────────────────┐
│                 External Clients                      │
│                                                       │
│  Web App  │  Mobile App  │  B2B Partners  │ Services │
└────┬──────┴──────┬───────┴────────┬────────┴────┬────┘
     │             │                │             │
     │ REST        │ GraphQL        │ SOAP        │ gRPC
     │             │                │             │
┌────▼─────────────▼────────────────▼─────────────▼────┐
│              Presentation Layer                       │
│                                                       │
│  OrderRestController  │  OrderGraphQLController      │
│  OrderSoapEndpoint    │  NotificationGrpcService     │
└───────────────────────┬───────────────────────────────┘
                        │
                        │ Dependency Injection
                        │
┌───────────────────────▼───────────────────────────────┐
│              Service Layer                            │
│                                                       │
│              OrderService (Interface)                 │
│                       ▲                               │
│                       │                               │
│              OrderServiceImpl                         │
│         (Business Logic Implementation)               │
└───────────────────────┬───────────────────────────────┘
                        │
                        │ Dependency Injection
                        │
┌───────────────────────▼───────────────────────────────┐
│            Repository Layer                           │
│                                                       │
│  OrderRepository  │  ClientRepository                │
│  ProductRepository │  OrderItemRepository            │
└───────────────────────┬───────────────────────────────┘
                        │
                        │ JPA/Hibernate
                        │
┌───────────────────────▼───────────────────────────────┐
│                  H2 Database                          │
└───────────────────────────────────────────────────────┘
```

## Sequence Diagrams

### Order Creation Flow

```
Client          REST Controller    OrderService    Repository    gRPC Client
  │                   │                 │              │              │
  ├──POST /orders────►│                 │              │              │
  │                   │                 │              │              │
  │                   ├─createOrder()──►│              │              │
  │                   │                 │              │              │
  │                   │                 ├─validate────►│              │
  │                   │                 │  client      │              │
  │                   │                 │◄─────────────┤              │
  │                   │                 │              │              │
  │                   │                 ├─validate────►│              │
  │                   │                 │  products    │              │
  │                   │                 │◄─────────────┤              │
  │                   │                 │              │              │
  │                   │                 ├─update───────►│              │
  │                   │                 │  stock       │              │
  │                   │                 │◄─────────────┤              │
  │                   │                 │              │              │
  │                   │                 ├─save(order)─►│              │
  │                   │                 │◄─────────────┤              │
  │                   │                 │              │              │
  │                   │                 ├─notify──────────────────────►│
  │                   │                 │              │              │
  │                   │                 │◄─────────────────────────────┤
  │                   │◄────────────────┤              │              │
  │◄──201 Created────┤                 │              │              │
  │   {order}         │                 │              │              │
```

### GraphQL Query Flow

```
Client          GraphQL Controller   OrderService    Repository
  │                   │                   │              │
  ├──GraphQL Query───►│                   │              │
  │  { orders {...} } │                   │              │
  │                   │                   │              │
  │                   ├─getAllOrders()───►│              │
  │                   │                   │              │
  │                   │                   ├─findAll()───►│
  │                   │                   │              │
  │                   │                   │◄─[orders]────┤
  │                   │◄──────────────────┤              │
  │                   │                   │              │
  │                   ├─(resolve fields)──┤              │
  │                   │                   │              │
  │◄──JSON Response───┤                   │              │
  │   {data: {...}}   │                   │              │
```

## Data Model

### Entity Relationship Diagram

```
┌─────────────────┐
│     Client      │
├─────────────────┤
│ * id            │
│   name          │
│   email         │
│   phone         │
│   address       │
└────────┬────────┘
         │ 1
         │
         │ *
         │
┌────────▼────────┐         ┌─────────────────┐
│     Order       │         │    OrderItem    │
├─────────────────┤    *    ├─────────────────┤
│ * id            │◄────────┤ * id            │
│   orderDate     │ 1       │   quantity      │
│   status        │         │   price         │
│   totalAmount   │         └────────┬────────┘
│   shippingAddr  │                  │ *
└─────────────────┘                  │
                                     │ 1
                           ┌─────────▼────────┐
                           │     Product      │
                           ├──────────────────┤
                           │ * id             │
                           │   name           │
                           │   description    │
                           │   price          │
                           │   stockQuantity  │
                           │   category       │
                           └──────────────────┘
```

### Order Status State Machine

```
    PENDING
       │
       ▼
   CONFIRMED
       │
       ▼
   PROCESSING
       │
       ▼
    SHIPPED
       │
       ▼
   DELIVERED
   
   (Any status except DELIVERED can transition to CANCELLED)
```

## API Protocols

### 1. REST API
- **Protocol**: HTTP/HTTPS
- **Format**: JSON
- **Use Case**: Web applications, mobile apps
- **Advantages**: 
  - Stateless
  - Cacheable
  - Well-understood
  - Easy to test

### 2. SOAP API
- **Protocol**: HTTP/HTTPS
- **Format**: XML
- **Use Case**: Legacy systems, enterprise integration
- **Advantages**:
  - Strict contracts (WSDL)
  - Built-in error handling
  - WS-* standards support

### 3. GraphQL API
- **Protocol**: HTTP/HTTPS
- **Format**: JSON
- **Use Case**: Complex queries, mobile apps with limited bandwidth
- **Advantages**:
  - Flexible queries
  - No over-fetching
  - Strong typing
  - Single endpoint

### 4. gRPC API
- **Protocol**: HTTP/2
- **Format**: Protocol Buffers
- **Use Case**: Microservices communication, real-time notifications
- **Advantages**:
  - High performance
  - Bi-directional streaming
  - Code generation
  - Compact binary format

## Design Principles Applied

### 1. **Single Responsibility Principle (SRP)**
Each class has one reason to change:
- Controllers handle HTTP/protocol concerns
- Services handle business logic
- Repositories handle data access
- Models represent data structures

### 2. **Open/Closed Principle (OCP)**
The system is open for extension, closed for modification:
- New API protocols can be added without changing business logic
- New repositories can be added by extending JpaRepository

### 3. **Dependency Inversion Principle (DIP)**
High-level modules don't depend on low-level modules:
- Controllers depend on Service interfaces, not implementations
- Services depend on Repository interfaces, not concrete classes

### 4. **Interface Segregation Principle (ISP)**
Clients don't depend on interfaces they don't use:
- OrderService interface only exposes order-related operations
- Repositories expose only necessary query methods

## Technology Stack Justification

| Technology | Purpose | Justification |
|------------|---------|---------------|
| Spring Boot | Framework | Rapid development, auto-configuration, production-ready |
| Spring Data JPA | Data Access | Reduces boilerplate, query derivation, pagination support |
| Spring Data REST | Auto REST API | Automatic CRUD endpoints with HATEOAS |
| Spring Web Services | SOAP | Standard SOAP support with Spring ecosystem |
| Spring GraphQL | GraphQL | Official Spring GraphQL integration |
| gRPC | RPC | High-performance inter-service communication |
| H2 Database | Database | Easy setup, good for development and demos |
| Lombok | Code Generation | Reduces boilerplate (getters, setters, constructors) |

## Security Considerations (Future Enhancements)

1. **Authentication & Authorization**
   - Add Spring Security
   - Implement JWT tokens
   - OAuth2 for third-party integrations

2. **API Rate Limiting**
   - Prevent abuse
   - Implement using Spring Cloud Gateway or Bucket4j

3. **Data Validation**
   - Input validation using Bean Validation
   - SQL injection prevention (JPA provides this)

4. **HTTPS/TLS**
   - Enforce encrypted communication in production

## Scalability Considerations

1. **Database**: Replace H2 with PostgreSQL/MySQL for production
2. **Caching**: Add Redis for frequently accessed data
3. **Load Balancing**: Deploy multiple instances behind a load balancer
4. **Message Queue**: Use RabbitMQ/Kafka for asynchronous processing
5. **Monitoring**: Add Prometheus, Grafana, ELK stack

## Testing Strategy

1. **Unit Tests**: Test business logic in services
2. **Integration Tests**: Test repository queries
3. **API Tests**: Test REST/GraphQL endpoints
4. **Contract Tests**: Test SOAP WSDL and gRPC proto contracts


