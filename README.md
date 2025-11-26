# Order Management System - Multi-Channel API

## 📋 Project Description

A comprehensive multi-channel order management system that exposes APIs through **REST, SOAP, GraphQL, and gRPC**. This project demonstrates enterprise-level integration patterns and modern API design principles using Spring Boot.



**Access Points:**
- REST API: http://localhost:8080/api/orders
- GraphiQL: http://localhost:8080/graphiql
- SOAP WSDL: http://localhost:8080/ws/orders.wsdl
- H2 Console: http://localhost:8080/h2-console
- gRPC: localhost:9090



## 🏗️ Architecture

### Layered Architecture
- **Presentation Layer**: REST, SOAP, GraphQL controllers and gRPC services
- **Business Logic Layer**: OrderService interface with implementation
- **Data Access Layer**: Spring Data JPA repositories
- **Database**: H2 in-memory database

### Design Patterns
- **Inversion of Control (IoC)**: Dependency Injection via Spring
- **Repository Pattern**: Spring Data JPA
- **Service Layer Pattern**: Business logic separation
- **DTO Pattern**: SOAP/GraphQL schema objects

## 🚀 Technologies Used

- **Spring Boot 4.0.0** (Java 21)
- **Spring Data JPA** - Database access
- **Spring Data REST** - Automatic CRUD REST APIs
- **Spring Web Services** - SOAP support
- **Spring GraphQL** - GraphQL API
- **gRPC** (net.devh:grpc-spring-boot-starter) - High-performance RPC
- **H2 Database** - In-memory database
- **Lombok** - Reduce boilerplate code
- **Maven** - Build and dependency management

## 📦 Project Structure

```
src/main/java/com/example/order_management/
├── model/                      # JPA Entities
│   ├── Client.java
│   ├── Order.java
│   ├── OrderItem.java
│   └── Product.java
├── repository/                 # Spring Data Repositories
│   ├── ClientRepository.java
│   ├── OrderRepository.java
│   ├── OrderItemRepository.java
│   └── ProductRepository.java
├── service/                    # Business Logic
│   ├── OrderService.java
│   └── impl/
│       └── OrderServiceImpl.java
├── web/
│   ├── rest/                  # REST Controllers
│   │   └── OrderRestController.java
│   ├── soap/                  # SOAP Endpoints
│   │   └── OrderSoapEndpoint.java
│   └── graphql/               # GraphQL Controllers
│       └── OrderGraphQLController.java
├── grpc/                      # gRPC Services
│   ├── NotificationGrpcService.java
│   └── NotificationGrpcClient.java
└── config/                    # Configuration Classes
    ├── WebServiceConfig.java
    └── DataInitializer.java

src/main/resources/
├── application.yaml           # Application configuration
├── graphql/
│   └── schema.graphqls       # GraphQL schema
├── proto/
│   └── notification.proto    # gRPC protocol definition
└── xsd/
    └── orders.xsd            # SOAP WSDL schema
```

## 🔧 Installation & Setup

### Prerequisites
- Java 21 or higher
- Maven 3.6+

### Build the Project

```powershell
# Navigate to project directory
cd C:\Users\Ismail\Desktop\order-management

# Clean and compile (this generates SOAP and gRPC classes)
mvn clean compile

# Package the application
mvn package

# Run the application
mvn spring-boot:run
```

The application will start on:
- **HTTP Server**: http://localhost:8080
- **gRPC Server**: localhost:9090

## 📡 API Documentation

### 1. REST API

**Base URL**: `http://localhost:8080/api/orders`

#### Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/orders` | Create a new order |
| GET | `/api/orders` | Get all orders |
| GET | `/api/orders/{id}` | Get order by ID |
| GET | `/api/orders/client/{clientId}` | Get orders by client |
| GET | `/api/orders/{id}/total` | Calculate order total |
| PUT | `/api/orders/{id}/status?status=CONFIRMED` | Update order status |
| PUT | `/api/orders/{id}/cancel` | Cancel an order |

#### Example: Create Order

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "client": {"id": 1},
    "shippingAddress": "123 Main St",
    "items": [
      {
        "product": {"id": 1},
        "quantity": 2,
        "price": 1299.99
      }
    ]
  }'
```

### 2. Spring Data REST

**Base URL**: `http://localhost:8080/`

Automatic CRUD endpoints for:
- `/clients` - Client management
- `/products` - Product management
- `/orders` - Order management

### 3. SOAP API

**WSDL URL**: `http://localhost:8080/ws/orders.wsdl`

**Endpoint**: `http://localhost:8080/ws`

#### Operations
- `getOrder` - Retrieve order by ID
- `getOrdersByClient` - Get all orders for a client
- `calculateOrderTotal` - Calculate order total amount
- `updateOrderStatus` - Update order status

#### Example: SOAP Request

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ord="http://example.com/order-management/orders">
   <soapenv:Header/>
   <soapenv:Body>
      <ord:getOrderRequest>
         <ord:orderId>1</ord:orderId>
      </ord:getOrderRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

### 4. GraphQL API

**Endpoint**: `http://localhost:8080/graphql`

**GraphiQL UI**: `http://localhost:8080/graphiql`

#### Queries

```graphql
# Get all orders
query {
  orders {
    id
    orderDate
    status
    totalAmount
    client {
      name
      email
    }
    items {
      product {
        name
      }
      quantity
      price
    }
  }
}

# Get orders by client
query {
  ordersByClient(clientId: 1) {
    id
    orderDate
    status
    totalAmount
  }
}

# Calculate order total
query {
  orderTotal(orderId: 1)
}
```

#### Mutations

```graphql
# Create order
mutation {
  createOrder(input: {
    clientId: 1
    shippingAddress: "123 Main St"
    items: [
      {productId: 1, quantity: 2}
      {productId: 2, quantity: 1}
    ]
  }) {
    id
    totalAmount
    status
  }
}

# Update order status
mutation {
  updateOrderStatus(orderId: 1, status: CONFIRMED) {
    id
    status
  }
}

# Cancel order
mutation {
  cancelOrder(orderId: 1) {
    id
    status
  }
}
```

### 5. gRPC API

**Server**: `localhost:9090`

**Protocol**: `notification.proto`

#### Service: NotificationService

**Methods**:
- `SendOrderNotification` - Send notification for order events
- `GetNotificationHistory` - Retrieve notification history

The gRPC notification service is automatically triggered when orders are created.

## 🗄️ Database

### H2 Console
- **URL**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:ordersdb`
- **Username**: `sa`
- **Password**: (empty)

### Sample Data

The application automatically initializes with:
- **3 Clients**: John Doe, Jane Smith, Bob Johnson
- **5 Products**: Electronics and accessories with varying prices

## 🧪 Testing

### Test with Postman

1. **Import REST endpoints**:
   - Create a new collection
   - Add requests for each REST endpoint
   - Test CRUD operations

2. **Test SOAP with Postman**:
   - Create POST request to `http://localhost:8080/ws`
   - Set Content-Type to `text/xml`
   - Use SOAP XML body

3. **Test GraphQL**:
   - Use GraphiQL interface at http://localhost:8080/graphiql
   - Or use Postman GraphQL feature

### Test with cURL

```powershell
# Get all products (Spring Data REST)
curl http://localhost:8080/products

# Create order (REST)
curl -X POST http://localhost:8080/api/orders `
  -H "Content-Type: application/json" `
  -d '{\"client\":{\"id\":1},\"items\":[{\"product\":{\"id\":1},\"quantity\":2}]}'

# GraphQL Query
curl -X POST http://localhost:8080/graphql `
  -H "Content-Type: application/json" `
  -d '{\"query\":\"{ orders { id totalAmount } }\"}'
```

## 📊 Architecture Diagrams

### Component Diagram
```
┌─────────────────────────────────────────────────┐
│           Client Applications                    │
│  (Web, Mobile, B2B Partners, Services)          │
└────────┬────────┬────────┬────────┬─────────────┘
         │        │        │        │
    ┌────▼───┐ ┌──▼───┐ ┌─▼────┐ ┌─▼────┐
    │  REST  │ │ SOAP │ │GraphQL│ │gRPC │
    └────┬───┘ └──┬───┘ └─┬────┘ └─┬────┘
         │        │        │        │
         └────────┴────────┴────────┘
                    │
         ┌──────────▼──────────┐
         │   Service Layer     │
         │  (OrderService)     │
         └──────────┬──────────┘
                    │
         ┌──────────▼──────────┐
         │  Repository Layer   │
         │  (Spring Data JPA)  │
         └──────────┬──────────┘
                    │
         ┌──────────▼──────────┐
         │   H2 Database       │
         └─────────────────────┘
```

## 🔍 Key Features

1. **Multi-Channel Support**: Same business logic exposed through 4 different protocols
2. **Automatic CRUD**: Spring Data REST provides automatic endpoints
3. **IoC/DI**: Proper use of Spring dependency injection
4. **Transaction Management**: @Transactional support
5. **Validation**: Business rules and stock management
6. **Event Notifications**: gRPC integration for order notifications
7. **Flexible Queries**: GraphQL for precise data fetching
8. **Legacy Support**: SOAP for integration with older systems

## 📝 Business Logic

### Order Creation Flow
1. Validate client exists
2. Validate products exist and have sufficient stock
3. Create order items with current product prices
4. Update product stock quantities
5. Calculate order total
6. Save order to database
7. Send notification via gRPC

### Order Status States
- `PENDING` - Initial state
- `CONFIRMED` - Order confirmed
- `PROCESSING` - Being processed
- `SHIPPED` - Shipped to customer
- `DELIVERED` - Delivered successfully
- `CANCELLED` - Order cancelled (restores stock)

## 🛠️ Configuration

### application.yaml

Key configurations:
- Server port: 8080
- gRPC port: 9090
- Database: H2 in-memory
- GraphQL endpoint: /graphql
- GraphiQL UI: /graphiql

## 📚 Learning Outcomes

This project demonstrates:
- ✅ Spring Boot application structure
- ✅ Layered architecture design
- ✅ Inversion of Control (IoC) principles
- ✅ Multiple API protocols (REST, SOAP, GraphQL, gRPC)
- ✅ JPA entity relationships
- ✅ Spring Data repositories
- ✅ Service layer patterns
- ✅ Transaction management
- ✅ Code generation (JAXB for SOAP, Protobuf for gRPC)

## 📄 License

This is an educational project for learning multi-channel API development.

## 👥 Author

Created as part of a multi-channel order management system educational project.

---

**Note**: This is a demonstration project using an in-memory database. For production use, configure a persistent database (PostgreSQL, MySQL, etc.) and add security (Spring Security, OAuth2).

