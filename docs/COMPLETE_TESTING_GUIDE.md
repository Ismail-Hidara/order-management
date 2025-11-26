# Complete Testing Guide - Order Management System

## Table of Contents
1. [Prerequisites](#prerequisites)
2. [Starting the Application](#starting-the-application)
3. [Testing Tools Setup](#testing-tools-setup)
4. [REST API Testing](#rest-api-testing)
5. [SOAP API Testing](#soap-api-testing)
6. [GraphQL API Testing](#graphql-api-testing)
7. [Spring Data REST Testing](#spring-data-rest-testing)
8. [gRPC Testing](#grpc-testing)
9. [Complete Test Scenarios](#complete-test-scenarios)
10. [Automated Testing Scripts](#automated-testing-scripts)

---

## Prerequisites

### Required Software
- ✅ Java 21+
- ✅ Maven (or use included Maven Wrapper)
- ✅ Web browser
- ✅ Postman (optional but recommended)
- ✅ cURL or PowerShell

### Check Installation
```powershell
# Check Java
java -version

# Check Maven
mvn -version
# OR use Maven Wrapper
.\mvnw.cmd -version
```

---

## Starting the Application

### Option 1: Using Build Script
```powershell
cd C:\Users\Ismail\Desktop\order-management
.\build.ps1
```

### Option 2: Using Maven Directly
```powershell
cd C:\Users\Ismail\Desktop\order-management
.\mvnw.cmd clean spring-boot:run
```

### Option 3: Using JAR File
```powershell
.\mvnw.cmd clean package
java -jar target/order-management-0.0.1-SNAPSHOT.jar
```

### Verify Application Started
Wait for the message:
```
Started OrderManagementApplication in X.XXX seconds
```

Check the endpoints are accessible:
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/products" -Method Get
```

---

## Testing Tools Setup

### 1. Postman Setup

#### Import Collection
1. Open Postman
2. Click **Import**
3. Create a new collection: "Order Management APIs"
4. Set base URL variable: `{{base_url}}` = `http://localhost:8080`

#### Environment Variables
Create these variables:
- `base_url`: `http://localhost:8080`
- `order_id`: `1` (will be set dynamically)
- `client_id`: `1`
- `product_id`: `1`

### 2. cURL Setup
cURL comes pre-installed on Windows 10+. Test it:
```powershell
curl --version
```

### 3. PowerShell Setup
PowerShell is built-in on Windows. We'll use `Invoke-RestMethod` and `Invoke-WebRequest`.

### 4. Browser Tools
- Chrome DevTools (F12)
- Firefox Developer Tools (F12)

---

## REST API Testing

### Base URL
```
http://localhost:8080/api/orders
```

### 1. Create Order (POST)

#### PowerShell
```powershell
$order = @{
    client = @{ id = 1 }
    shippingAddress = "123 Main Street, New York, NY 10001"
    items = @(
        @{
            product = @{ id = 1 }
            quantity = 2
        },
        @{
            product = @{ id = 2 }
            quantity = 1
        }
    )
} | ConvertTo-Json -Depth 10

$response = Invoke-RestMethod -Uri "http://localhost:8080/api/orders" `
    -Method Post `
    -ContentType "application/json" `
    -Body $order

$response | ConvertTo-Json -Depth 10
```

#### cURL
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "client": {"id": 1},
    "shippingAddress": "123 Main Street, New York, NY 10001",
    "items": [
      {"product": {"id": 1}, "quantity": 2},
      {"product": {"id": 2}, "quantity": 1}
    ]
  }'
```

#### Expected Response
```json
{
  "id": 1,
  "client": {
    "id": 1,
    "name": "John Doe",
    "email": "john.doe@example.com",
    "phone": "+1234567890",
    "address": "789 Pine St, Seattle, WA"
  },
  "orderDate": "2025-11-26T23:15:00",
  "status": "PENDING",
  "items": [
    {
      "id": 1,
      "product": {
        "id": 1,
        "name": "Laptop Pro 15",
        "description": "High-performance laptop with 15-inch display",
        "price": 1299.99,
        "stockQuantity": 48,
        "category": "Electronics"
      },
      "quantity": 2,
      "price": 1299.99
    },
    {
      "id": 2,
      "product": {
        "id": 2,
        "name": "Wireless Mouse",
        "description": "Ergonomic wireless mouse with USB receiver",
        "price": 29.99,
        "stockQuantity": 199,
        "category": "Electronics"
      },
      "quantity": 1,
      "price": 29.99
    }
  ],
  "totalAmount": 2629.97,
  "shippingAddress": "123 Main Street, New York, NY 10001"
}
```

### 2. Get All Orders (GET)

#### PowerShell
```powershell
$orders = Invoke-RestMethod -Uri "http://localhost:8080/api/orders" -Method Get
$orders | ConvertTo-Json -Depth 10
```

#### cURL
```bash
curl -X GET http://localhost:8080/api/orders
```

### 3. Get Order by ID (GET)

#### PowerShell
```powershell
$order = Invoke-RestMethod -Uri "http://localhost:8080/api/orders/1" -Method Get
$order | ConvertTo-Json -Depth 10
```

#### cURL
```bash
curl -X GET http://localhost:8080/api/orders/1
```

### 4. Get Orders by Client (GET)

#### PowerShell
```powershell
$clientOrders = Invoke-RestMethod -Uri "http://localhost:8080/api/orders/client/1" -Method Get
$clientOrders | ConvertTo-Json -Depth 10
```

#### cURL
```bash
curl -X GET http://localhost:8080/api/orders/client/1
```

### 5. Get Order Total (GET)

#### PowerShell
```powershell
$total = Invoke-RestMethod -Uri "http://localhost:8080/api/orders/1/total" -Method Get
Write-Host "Order Total: `$$total"
```

#### cURL
```bash
curl -X GET http://localhost:8080/api/orders/1/total
```

### 6. Update Order Status (PUT)

#### PowerShell - Test All Status Transitions
```powershell
# PENDING → CONFIRMED
$order = Invoke-RestMethod -Uri "http://localhost:8080/api/orders/1/status?status=CONFIRMED" -Method Put
Write-Host "Status: $($order.status)"

# CONFIRMED → PROCESSING
$order = Invoke-RestMethod -Uri "http://localhost:8080/api/orders/1/status?status=PROCESSING" -Method Put
Write-Host "Status: $($order.status)"

# PROCESSING → SHIPPED
$order = Invoke-RestMethod -Uri "http://localhost:8080/api/orders/1/status?status=SHIPPED" -Method Put
Write-Host "Status: $($order.status)"

# SHIPPED → DELIVERED
$order = Invoke-RestMethod -Uri "http://localhost:8080/api/orders/1/status?status=DELIVERED" -Method Put
Write-Host "Status: $($order.status)"
```

#### cURL
```bash
curl -X PUT "http://localhost:8080/api/orders/1/status?status=CONFIRMED"
curl -X PUT "http://localhost:8080/api/orders/1/status?status=PROCESSING"
curl -X PUT "http://localhost:8080/api/orders/1/status?status=SHIPPED"
curl -X PUT "http://localhost:8080/api/orders/1/status?status=DELIVERED"
```

### 7. Cancel Order (PUT)

#### PowerShell
```powershell
$cancelledOrder = Invoke-RestMethod -Uri "http://localhost:8080/api/orders/2/cancel" -Method Put
$cancelledOrder | ConvertTo-Json -Depth 10
```

#### cURL
```bash
curl -X PUT http://localhost:8080/api/orders/2/cancel
```

---

## SOAP API Testing

### Base URL
```
http://localhost:8080/ws
```

### WSDL Access
```
http://localhost:8080/ws/orders.wsdl
```

### 1. Get Order (SOAP)

#### PowerShell
```powershell
$soapRequest = @"
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ord="http://example.com/order-management/orders">
   <soapenv:Header/>
   <soapenv:Body>
      <ord:getOrderRequest>
         <ord:orderId>1</ord:orderId>
      </ord:getOrderRequest>
   </soapenv:Body>
</soapenv:Envelope>
"@

$response = Invoke-WebRequest -Uri "http://localhost:8080/ws" `
    -Method Post `
    -ContentType "text/xml" `
    -Body $soapRequest

$response.Content
```

#### cURL
```bash
curl -X POST http://localhost:8080/ws \
  -H "Content-Type: text/xml" \
  -d '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ord="http://example.com/order-management/orders">
   <soapenv:Header/>
   <soapenv:Body>
      <ord:getOrderRequest>
         <ord:orderId>1</ord:orderId>
      </ord:getOrderRequest>
   </soapenv:Body>
</soapenv:Envelope>'
```

#### Expected Response
```xml
<?xml version="1.0" encoding="UTF-8"?>
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
   <SOAP-ENV:Body>
      <ns2:getOrderResponse xmlns:ns2="http://example.com/order-management/orders">
         <ns2:order>
            <ns2:id>1</ns2:id>
            <ns2:clientId>1</ns2:clientId>
            <ns2:clientName>John Doe</ns2:clientName>
            <ns2:orderDate>2025-11-26T23:15:00</ns2:orderDate>
            <ns2:status>PENDING</ns2:status>
            <ns2:totalAmount>2629.97</ns2:totalAmount>
            <ns2:shippingAddress>123 Main Street, New York, NY 10001</ns2:shippingAddress>
         </ns2:order>
      </ns2:getOrderResponse>
   </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```

### 2. Get Orders by Client (SOAP)

#### PowerShell
```powershell
$soapRequest = @"
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ord="http://example.com/order-management/orders">
   <soapenv:Header/>
   <soapenv:Body>
      <ord:getOrdersByClientRequest>
         <ord:clientId>1</ord:clientId>
      </ord:getOrdersByClientRequest>
   </soapenv:Body>
</soapenv:Envelope>
"@

$response = Invoke-WebRequest -Uri "http://localhost:8080/ws" `
    -Method Post `
    -ContentType "text/xml" `
    -Body $soapRequest

$response.Content
```

### 3. Calculate Order Total (SOAP)

#### PowerShell
```powershell
$soapRequest = @"
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ord="http://example.com/order-management/orders">
   <soapenv:Header/>
   <soapenv:Body>
      <ord:calculateOrderTotalRequest>
         <ord:orderId>1</ord:orderId>
      </ord:calculateOrderTotalRequest>
   </soapenv:Body>
</soapenv:Envelope>
"@

$response = Invoke-WebRequest -Uri "http://localhost:8080/ws" `
    -Method Post `
    -ContentType "text/xml" `
    -Body $soapRequest

$response.Content
```

### 4. Update Order Status (SOAP)

#### PowerShell - All Status Values
```powershell
# Status values: PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED

$soapRequest = @"
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ord="http://example.com/order-management/orders">
   <soapenv:Header/>
   <soapenv:Body>
      <ord:updateOrderStatusRequest>
         <ord:orderId>1</ord:orderId>
         <ord:status>CONFIRMED</ord:status>
      </ord:updateOrderStatusRequest>
   </soapenv:Body>
</soapenv:Envelope>
"@

$response = Invoke-WebRequest -Uri "http://localhost:8080/ws" `
    -Method Post `
    -ContentType "text/xml" `
    -Body $soapRequest

$response.Content
```

---

## GraphQL API Testing

### Access GraphiQL Interface
```
http://localhost:8080/graphiql
```

### GraphQL Endpoint
```
http://localhost:8080/graphql
```

### 1. Query All Orders

#### GraphQL Query
```graphql
query GetAllOrders {
  orders {
    id
    orderDate
    status
    totalAmount
    shippingAddress
    client {
      id
      name
      email
      phone
      address
    }
    items {
      id
      quantity
      price
      product {
        id
        name
        description
        price
        stockQuantity
        category
      }
    }
  }
}
```

#### PowerShell
```powershell
$query = @{
    query = @"
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
"@
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "http://localhost:8080/graphql" `
    -Method Post `
    -ContentType "application/json" `
    -Body $query

$response.data.orders | ConvertTo-Json -Depth 10
```

### 2. Query Single Order

#### GraphQL Query
```graphql
query GetOrder($id: ID!) {
  order(id: $id) {
    id
    orderDate
    status
    totalAmount
    shippingAddress
    client {
      name
      email
    }
    items {
      product {
        name
        price
      }
      quantity
      price
    }
  }
}
```

#### Variables
```json
{
  "id": "1"
}
```

#### PowerShell
```powershell
$query = @{
    query = "query { order(id: 1) { id status totalAmount client { name } } }"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/graphql" `
    -Method Post `
    -ContentType "application/json" `
    -Body $query
```

### 3. Query Orders by Client

#### GraphQL Query
```graphql
query GetClientOrders($clientId: ID!) {
  ordersByClient(clientId: $clientId) {
    id
    orderDate
    status
    totalAmount
    items {
      product {
        name
      }
      quantity
    }
  }
}
```

#### Variables
```json
{
  "clientId": "1"
}
```

### 4. Query Order Total

#### GraphQL Query
```graphql
query GetOrderTotal($orderId: ID!) {
  orderTotal(orderId: $orderId)
}
```

### 5. Query All Clients

#### GraphQL Query
```graphql
query GetAllClients {
  clients {
    id
    name
    email
    phone
    address
    orders {
      id
      orderDate
      status
      totalAmount
    }
  }
}
```

#### PowerShell
```powershell
$query = @{
    query = "query { clients { id name email phone address } }"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/graphql" `
    -Method Post `
    -ContentType "application/json" `
    -Body $query
```

### 6. Query All Products

#### GraphQL Query
```graphql
query GetAllProducts {
  products {
    id
    name
    description
    price
    stockQuantity
    category
  }
}
```

#### PowerShell
```powershell
$query = @{
    query = "query { products { id name price stockQuantity } }"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/graphql" `
    -Method Post `
    -ContentType "application/json" `
    -Body $query
```

### 7. Mutation: Create Order

#### GraphQL Mutation
```graphql
mutation CreateOrder($input: CreateOrderInput!) {
  createOrder(input: $input) {
    id
    orderDate
    status
    totalAmount
    shippingAddress
    client {
      name
      email
    }
    items {
      product {
        name
        price
      }
      quantity
      price
    }
  }
}
```

#### Variables
```json
{
  "input": {
    "clientId": "1",
    "shippingAddress": "456 Oak Avenue, Los Angeles, CA",
    "items": [
      {
        "productId": "1",
        "quantity": 1
      },
      {
        "productId": "3",
        "quantity": 2
      }
    ]
  }
}
```

#### PowerShell
```powershell
$mutation = @{
    query = @"
mutation {
  createOrder(input: {
    clientId: 1
    shippingAddress: "456 Oak Avenue, Los Angeles, CA"
    items: [
      { productId: 1, quantity: 1 },
      { productId: 3, quantity: 2 }
    ]
  }) {
    id
    status
    totalAmount
    client { name }
  }
}
"@
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "http://localhost:8080/graphql" `
    -Method Post `
    -ContentType "application/json" `
    -Body $mutation

$response.data.createOrder | ConvertTo-Json
```

### 8. Mutation: Update Order Status

#### GraphQL Mutation
```graphql
mutation UpdateStatus($orderId: ID!, $status: OrderStatus!) {
  updateOrderStatus(orderId: $orderId, status: $status) {
    id
    status
    orderDate
    totalAmount
  }
}
```

#### Variables
```json
{
  "orderId": "1",
  "status": "CONFIRMED"
}
```

#### PowerShell - Test All Statuses
```powershell
$statuses = @("CONFIRMED", "PROCESSING", "SHIPPED", "DELIVERED")

foreach ($status in $statuses) {
    $mutation = @{
        query = "mutation { updateOrderStatus(orderId: 1, status: $status) { id status } }"
    } | ConvertTo-Json
    
    $response = Invoke-RestMethod -Uri "http://localhost:8080/graphql" `
        -Method Post `
        -ContentType "application/json" `
        -Body $mutation
    
    Write-Host "Updated to: $($response.data.updateOrderStatus.status)"
}
```

### 9. Mutation: Cancel Order

#### GraphQL Mutation
```graphql
mutation CancelOrder($orderId: ID!) {
  cancelOrder(orderId: $orderId) {
    id
    status
    totalAmount
  }
}
```

#### PowerShell
```powershell
$mutation = @{
    query = "mutation { cancelOrder(orderId: 2) { id status totalAmount } }"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/graphql" `
    -Method Post `
    -ContentType "application/json" `
    -Body $mutation
```

### 10. Complex Query: Client with All Orders

#### GraphQL Query
```graphql
query GetClientWithOrders($clientId: ID!) {
  client(id: $clientId) {
    id
    name
    email
    phone
    address
    orders {
      id
      orderDate
      status
      totalAmount
      shippingAddress
      items {
        product {
          name
          price
        }
        quantity
        price
      }
    }
  }
}
```

---

## Spring Data REST Testing

### Base URLs
```
http://localhost:8080/clients
http://localhost:8080/products
http://localhost:8080/orders
http://localhost:8080/orderItems
```

### 1. Get All Clients

#### PowerShell
```powershell
$clients = Invoke-RestMethod -Uri "http://localhost:8080/clients" -Method Get
$clients._embedded.clients | Format-Table
```

#### With Pagination
```powershell
$clients = Invoke-RestMethod -Uri "http://localhost:8080/clients?page=0&size=10&sort=name,asc" -Method Get
```

### 2. Get All Products

#### PowerShell
```powershell
$products = Invoke-RestMethod -Uri "http://localhost:8080/products" -Method Get
$products._embedded.products | Format-Table id, name, price, stockQuantity
```

### 3. Get All Orders

#### PowerShell
```powershell
$orders = Invoke-RestMethod -Uri "http://localhost:8080/orders" -Method Get
$orders._embedded.orders | Format-Table
```

### 4. Create Client (POST)

#### PowerShell
```powershell
$newClient = @{
    name = "Alice Williams"
    email = "alice.williams@example.com"
    phone = "+1555666777"
    address = "999 Elm Street, Boston, MA"
} | ConvertTo-Json

$client = Invoke-RestMethod -Uri "http://localhost:8080/clients" `
    -Method Post `
    -ContentType "application/json" `
    -Body $newClient

$client | ConvertTo-Json
```

### 5. Create Product (POST)

#### PowerShell
```powershell
$newProduct = @{
    name = "Webcam HD"
    description = "1080p HD webcam with built-in microphone"
    price = 79.99
    stockQuantity = 100
    category = "Electronics"
} | ConvertTo-Json

$product = Invoke-RestMethod -Uri "http://localhost:8080/products" `
    -Method Post `
    -ContentType "application/json" `
    -Body $newProduct

$product | ConvertTo-Json
```

### 6. Update Client (PUT)

#### PowerShell
```powershell
$updatedClient = @{
    name = "John Doe Updated"
    email = "john.doe.updated@example.com"
    phone = "+1234567890"
    address = "123 Main St, New York, NY 10001"
} | ConvertTo-Json

$client = Invoke-RestMethod -Uri "http://localhost:8080/clients/1" `
    -Method Put `
    -ContentType "application/json" `
    -Body $updatedClient

$client | ConvertTo-Json
```

### 7. Patch Client (PATCH)

#### PowerShell
```powershell
$partialUpdate = @{
    phone = "+1999888777"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/clients/1" `
    -Method Patch `
    -ContentType "application/json" `
    -Body $partialUpdate
```

### 8. Delete Client (DELETE)

#### PowerShell
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/clients/4" -Method Delete
Write-Host "Client deleted successfully"
```

### 9. Search Endpoints

#### PowerShell
```powershell
# Discover available search methods
$search = Invoke-RestMethod -Uri "http://localhost:8080/clients/search" -Method Get
$search._links | ConvertTo-Json

# Search by email (if implemented)
$client = Invoke-RestMethod -Uri "http://localhost:8080/clients/search/findByEmail?email=john.doe@example.com" -Method Get
```

---

## gRPC Testing

### gRPC Server
```
localhost:9090
```

### Using grpcurl (Install first)

#### Install grpcurl
```powershell
# Using Chocolatey
choco install grpcurl

# OR download from: https://github.com/fullstorydev/grpcurl/releases
```

#### List Services
```bash
grpcurl -plaintext localhost:9090 list
```

#### Describe Service
```bash
grpcurl -plaintext localhost:9090 describe notification.NotificationService
```

#### Send Order Notification
```bash
grpcurl -plaintext -d '{
  "order_id": 1,
  "client_id": 1,
  "client_name": "John Doe",
  "client_email": "john.doe@example.com",
  "total_amount": 1299.99,
  "status": "PENDING",
  "message": "Your order has been created"
}' localhost:9090 notification.NotificationService/SendOrderNotification
```

#### Get Notification History
```bash
grpcurl -plaintext -d '{
  "client_id": 1,
  "limit": 10
}' localhost:9090 notification.NotificationService/GetNotificationHistory
```

---

## Complete Test Scenarios

### Scenario 1: Complete Order Lifecycle

```powershell
# Step 1: Check available products
Write-Host "=== Step 1: Checking Products ===" -ForegroundColor Cyan
$products = Invoke-RestMethod -Uri "http://localhost:8080/products" -Method Get
$products._embedded.products | Format-Table id, name, price, stockQuantity

# Step 2: Check clients
Write-Host "`n=== Step 2: Checking Clients ===" -ForegroundColor Cyan
$clients = Invoke-RestMethod -Uri "http://localhost:8080/clients" -Method Get
$clients._embedded.clients | Format-Table id, name, email

# Step 3: Create order
Write-Host "`n=== Step 3: Creating Order ===" -ForegroundColor Cyan
$newOrder = @{
    client = @{ id = 1 }
    shippingAddress = "123 Test Street, Test City"
    items = @(
        @{ product = @{ id = 1 }; quantity = 1 },
        @{ product = @{ id = 2 }; quantity = 2 }
    )
} | ConvertTo-Json -Depth 10

$order = Invoke-RestMethod -Uri "http://localhost:8080/api/orders" `
    -Method Post `
    -ContentType "application/json" `
    -Body $newOrder

Write-Host "Order Created: ID = $($order.id), Total = `$$($order.totalAmount)" -ForegroundColor Green

# Step 4: Verify stock was reduced
Write-Host "`n=== Step 4: Verifying Stock Reduction ===" -ForegroundColor Cyan
$product1 = Invoke-RestMethod -Uri "http://localhost:8080/products/1" -Method Get
Write-Host "Product 1 stock: $($product1.stockQuantity)"

# Step 5: Update order through statuses
Write-Host "`n=== Step 5: Updating Order Status ===" -ForegroundColor Cyan
$statuses = @("CONFIRMED", "PROCESSING", "SHIPPED", "DELIVERED")
foreach ($status in $statuses) {
    Start-Sleep -Seconds 1
    $updated = Invoke-RestMethod -Uri "http://localhost:8080/api/orders/$($order.id)/status?status=$status" -Method Put
    Write-Host "Status updated to: $($updated.status)" -ForegroundColor Yellow
}

# Step 6: View order history
Write-Host "`n=== Step 6: Viewing Order History ===" -ForegroundColor Cyan
$history = Invoke-RestMethod -Uri "http://localhost:8080/api/orders/client/1" -Method Get
Write-Host "Total orders for client: $($history.Count)"
$history | Format-Table id, status, totalAmount

Write-Host "`n=== Test Complete ===" -ForegroundColor Green
```

### Scenario 2: Order Cancellation with Stock Restoration

```powershell
# Create order
$newOrder = @{
    client = @{ id = 1 }
    items = @(@{ product = @{ id = 1 }; quantity = 5 })
} | ConvertTo-Json -Depth 10

$order = Invoke-RestMethod -Uri "http://localhost:8080/api/orders" `
    -Method Post `
    -ContentType "application/json" `
    -Body $newOrder

# Check stock before cancellation
$productBefore = Invoke-RestMethod -Uri "http://localhost:8080/products/1" -Method Get
Write-Host "Stock before cancellation: $($productBefore.stockQuantity)"

# Cancel order
$cancelled = Invoke-RestMethod -Uri "http://localhost:8080/api/orders/$($order.id)/cancel" -Method Put
Write-Host "Order cancelled: $($cancelled.status)"

# Check stock after cancellation
$productAfter = Invoke-RestMethod -Uri "http://localhost:8080/products/1" -Method Get
Write-Host "Stock after cancellation: $($productAfter.stockQuantity)"
Write-Host "Stock restored: $($ productAfter.stockQuantity - $productBefore.stockQuantity) units"
```

### Scenario 3: Testing All GraphQL Operations

```powershell
Write-Host "=== Testing GraphQL API ===" -ForegroundColor Cyan

# Query
$query1 = @{ query = "{ products { id name price } }" } | ConvertTo-Json
$result1 = Invoke-RestMethod -Uri "http://localhost:8080/graphql" -Method Post -ContentType "application/json" -Body $query1
Write-Host "Products count: $($result1.data.products.Count)"

# Mutation - Create
$mutation1 = @{
    query = 'mutation { createOrder(input: {clientId: 1, items: [{productId: 1, quantity: 1}]}) { id status } }'
} | ConvertTo-Json
$result2 = Invoke-RestMethod -Uri "http://localhost:8080/graphql" -Method Post -ContentType "application/json" -Body $mutation1
Write-Host "Order created via GraphQL: ID = $($result2.data.createOrder.id)"

# Mutation - Update
$mutation2 = @{
    query = "mutation { updateOrderStatus(orderId: $($result2.data.createOrder.id), status: CONFIRMED) { id status } }"
} | ConvertTo-Json
$result3 = Invoke-RestMethod -Uri "http://localhost:8080/graphql" -Method Post -ContentType "application/json" -Body $mutation2
Write-Host "Order status: $($result3.data.updateOrderStatus.status)"

Write-Host "=== GraphQL Tests Complete ===" -ForegroundColor Green
```

---

## Automated Testing Scripts

### Save as: `test-all-apis.ps1`

```powershell
# Complete API Testing Script
param(
    [string]$BaseUrl = "http://localhost:8080"
)

$ErrorActionPreference = "Stop"
$testsPassed = 0
$testsFailed = 0

function Test-Endpoint {
    param(
        [string]$Name,
        [scriptblock]$TestBlock
    )
    
    Write-Host "`n=== Testing: $Name ===" -ForegroundColor Cyan
    try {
        & $TestBlock
        Write-Host "✓ PASSED" -ForegroundColor Green
        $script:testsPassed++
    } catch {
        Write-Host "✗ FAILED: $($_.Exception.Message)" -ForegroundColor Red
        $script:testsFailed++
    }
}

Write-Host "========================================" -ForegroundColor Yellow
Write-Host "Order Management System - API Tests" -ForegroundColor Yellow
Write-Host "========================================" -ForegroundColor Yellow

# Test 1: REST - Get Products
Test-Endpoint "REST: Get All Products" {
    $products = Invoke-RestMethod -Uri "$BaseUrl/products" -Method Get
    if ($products._embedded.products.Count -eq 0) { throw "No products found" }
}

# Test 2: REST - Get Clients
Test-Endpoint "REST: Get All Clients" {
    $clients = Invoke-RestMethod -Uri "$BaseUrl/clients" -Method Get
    if ($clients._embedded.clients.Count -eq 0) { throw "No clients found" }
}

# Test 3: REST - Create Order
Test-Endpoint "REST: Create Order" {
    $order = @{
        client = @{ id = 1 }
        items = @(@{ product = @{ id = 1 }; quantity = 1 })
    } | ConvertTo-Json -Depth 10
    
    $result = Invoke-RestMethod -Uri "$BaseUrl/api/orders" -Method Post -ContentType "application/json" -Body $order
    if (-not $result.id) { throw "Order not created" }
    $script:createdOrderId = $result.id
}

# Test 4: REST - Get Order by ID
Test-Endpoint "REST: Get Order by ID" {
    $order = Invoke-RestMethod -Uri "$BaseUrl/api/orders/$script:createdOrderId" -Method Get
    if ($order.id -ne $script:createdOrderId) { throw "Wrong order returned" }
}

# Test 5: REST - Update Status
Test-Endpoint "REST: Update Order Status" {
    $order = Invoke-RestMethod -Uri "$BaseUrl/api/orders/$script:createdOrderId/status?status=CONFIRMED" -Method Put
    if ($order.status -ne "CONFIRMED") { throw "Status not updated" }
}

# Test 6: GraphQL - Query Products
Test-Endpoint "GraphQL: Query Products" {
    $query = @{ query = "{ products { id name } }" } | ConvertTo-Json
    $result = Invoke-RestMethod -Uri "$BaseUrl/graphql" -Method Post -ContentType "application/json" -Body $query
    if ($result.data.products.Count -eq 0) { throw "No products found" }
}

# Test 7: GraphQL - Create Order
Test-Endpoint "GraphQL: Create Order" {
    $mutation = @{
        query = 'mutation { createOrder(input: {clientId: 1, items: [{productId: 1, quantity: 1}]}) { id } }'
    } | ConvertTo-Json
    $result = Invoke-RestMethod -Uri "$BaseUrl/graphql" -Method Post -ContentType "application/json" -Body $mutation
    if (-not $result.data.createOrder.id) { throw "Order not created" }
}

# Test 8: SOAP - Get Order
Test-Endpoint "SOAP: Get Order" {
    $soapRequest = @"
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ord="http://example.com/order-management/orders">
   <soapenv:Body>
      <ord:getOrderRequest>
         <ord:orderId>$script:createdOrderId</ord:orderId>
      </ord:getOrderRequest>
   </soapenv:Body>
</soapenv:Envelope>
"@
    $result = Invoke-WebRequest -Uri "$BaseUrl/ws" -Method Post -ContentType "text/xml" -Body $soapRequest
    if ($result.StatusCode -ne 200) { throw "SOAP request failed" }
}

# Summary
Write-Host "`n========================================" -ForegroundColor Yellow
Write-Host "Test Summary" -ForegroundColor Yellow
Write-Host "========================================" -ForegroundColor Yellow
Write-Host "Passed: $testsPassed" -ForegroundColor Green
Write-Host "Failed: $testsFailed" -ForegroundColor Red
Write-Host "Total: $($testsPassed + $testsFailed)" -ForegroundColor White

if ($testsFailed -eq 0) {
    Write-Host "`n✓ All tests passed!" -ForegroundColor Green
    exit 0
} else {
    Write-Host "`n✗ Some tests failed!" -ForegroundColor Red
    exit 1
}
```

### Run the automated tests:
```powershell
.\test-all-apis.ps1
```

---

## Test Results Verification

### Expected Outcomes

#### Successful Order Creation
- ✅ Order ID is generated
- ✅ Status is PENDING
- ✅ Total amount is calculated correctly
- ✅ Stock quantities are reduced
- ✅ gRPC notification is sent

#### Successful Status Update
- ✅ Order status changes
- ✅ Previous status validations work
- ✅ Cannot update delivered orders

#### Successful Order Cancellation
- ✅ Order status becomes CANCELLED
- ✅ Stock quantities are restored
- ✅ Cannot cancel delivered orders

### Common Issues and Solutions

| Issue | Solution |
|-------|----------|
| Connection refused | Ensure application is running |
| 404 Not Found | Check endpoint URL |
| 400 Bad Request | Validate request body JSON |
| 500 Internal Server Error | Check application logs |
| Stock not available | Product stock is insufficient |

---

## Performance Testing

### Load Testing with PowerShell

```powershell
$iterations = 100
$results = @()

for ($i = 1; $i -le $iterations; $i++) {
    $start = Get-Date
    
    $order = @{
        client = @{ id = 1 }
        items = @(@{ product = @{ id = 1 }; quantity = 1 })
    } | ConvertTo-Json -Depth 10
    
    try {
        Invoke-RestMethod -Uri "http://localhost:8080/api/orders" `
            -Method Post `
            -ContentType "application/json" `
            -Body $order | Out-Null
        
        $end = Get-Date
        $duration = ($end - $start).TotalMilliseconds
        $results += $duration
        
        Write-Progress -Activity "Load Testing" -Status "$i of $iterations" -PercentComplete (($i / $iterations) * 100)
    } catch {
        Write-Host "Request $i failed: $($_.Exception.Message)" -ForegroundColor Red
    }
}

Write-Host "`nLoad Test Results:" -ForegroundColor Cyan
Write-Host "Average: $([math]::Round(($results | Measure-Object -Average).Average, 2)) ms"
Write-Host "Min: $([math]::Round(($results | Measure-Object -Minimum).Minimum, 2)) ms"
Write-Host "Max: $([math]::Round(($results | Measure-Object -Maximum).Maximum, 2)) ms"
```

---

## Troubleshooting

### Check Application Status
```powershell
try {
    Invoke-RestMethod -Uri "http://localhost:8080/products" -Method Get
    Write-Host "✓ Application is running" -ForegroundColor Green
} catch {
    Write-Host "✗ Application is not responding" -ForegroundColor Red
}
```

### Issue: SOAP Error 500 - "no session"

**Problem**: SOAP endpoint returns error:
```xml
<SOAP-ENV:Fault>
  <faultcode>SOAP-ENV:Server</faultcode>
  <faultstring>Could not initialize proxy [com.example.order_management.model.Client#1] - no session</faultstring>
</SOAP-ENV:Fault>
```

**Cause**: Hibernate lazy loading issue - session closes before SOAP serialization.

**Solution**: This has been fixed with `@Transactional` annotations on SOAP endpoints.

If you still see this error:
1. Ensure you have the latest code
2. Verify `OrderSoapEndpoint.java` has `@Transactional` on all methods
3. Rebuild: `.\mvnw.cmd clean compile spring-boot:run`

**Details**: See [SOAP_LAZY_LOADING_FIX.md](SOAP_LAZY_LOADING_FIX.md) for complete explanation.

### Issue: Infinite JSON Loops (3000+ line responses)

**Problem**: REST API returns massive responses with circular references like:
```json
{
  "order": {
    "items": [
      { "order": { "items": [ ... ] } }
    ]
  }
}
```

**Solution**: This has been fixed with Jackson annotations. If you still see this:
1. Ensure you have the latest code:
   ```powershell
   git pull  # or re-download the project
   ```
2. Verify the annotations are in place:
   - `Order.java`: `@JsonManagedReference` on `items`
   - `OrderItem.java`: `@JsonBackReference` on `order`
   - `Client.java`: `@JsonIgnore` on `orders`
3. Rebuild the project:
   ```powershell
   .\mvnw.cmd clean compile spring-boot:run
   ```

**Details**: See [JSON_RECURSION_FIX.md](JSON_RECURSION_FIX.md) for complete explanation.

### View Application Logs
Check the console where you ran `spring-boot:run` for detailed logs.

### Check Database
Access H2 Console: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:ordersdb`
- Username: `sa`
- Password: (leave empty)
