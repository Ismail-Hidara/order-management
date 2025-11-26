package com.example.order_management.web.soap;

import com.example.order_management.model.Order;
import com.example.order_management.service.OrderService;
import com.example.order_management.orders.*;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.GregorianCalendar;
import java.util.List;

@Endpoint
@RequiredArgsConstructor
public class OrderSoapEndpoint {

    private static final String NAMESPACE_URI = "http://example.com/order-management/orders";

    private final OrderService orderService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getOrderRequest")
    @ResponsePayload
    @Transactional(readOnly = true)
    public GetOrderResponse getOrder(@RequestPayload GetOrderRequest request) {
        Order order = orderService.getOrderById(request.getOrderId());

        // Force initialization of lazy-loaded properties
        order.getClient().getName();
        order.getItems().size();

        GetOrderResponse response = new GetOrderResponse();
        response.setOrder(convertToSoapOrder(order));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getOrdersByClientRequest")
    @ResponsePayload
    @Transactional(readOnly = true)
    public GetOrdersByClientResponse getOrdersByClient(@RequestPayload GetOrdersByClientRequest request) {
        List<Order> orders = orderService.getOrdersByClientId(request.getClientId());

        // Force initialization of lazy-loaded properties
        orders.forEach(order -> {
            order.getClient().getName();
            order.getItems().size();
        });

        GetOrdersByClientResponse response = new GetOrdersByClientResponse();
        orders.forEach(order -> response.getOrders().add(convertToSoapOrder(order)));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "calculateOrderTotalRequest")
    @ResponsePayload
    @Transactional(readOnly = true)
    public CalculateOrderTotalResponse calculateOrderTotal(@RequestPayload CalculateOrderTotalRequest request) {
        BigDecimal total = orderService.calculateOrderTotal(request.getOrderId());

        CalculateOrderTotalResponse response = new CalculateOrderTotalResponse();
        response.setTotal(total);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateOrderStatusRequest")
    @ResponsePayload
    @Transactional
    public UpdateOrderStatusResponse updateOrderStatus(@RequestPayload UpdateOrderStatusRequest request) {
        Order.OrderStatus status = Order.OrderStatus.valueOf(request.getStatus().value());
        Order updatedOrder = orderService.updateOrderStatus(request.getOrderId(), status);

        // Force initialization of lazy-loaded properties
        updatedOrder.getClient().getName();
        updatedOrder.getItems().size();

        UpdateOrderStatusResponse response = new UpdateOrderStatusResponse();
        response.setOrder(convertToSoapOrder(updatedOrder));
        return response;
    }

    private com.example.order_management.orders.Order convertToSoapOrder(Order order) {
        com.example.order_management.orders.Order soapOrder = new com.example.order_management.orders.Order();
        soapOrder.setId(order.getId());
        soapOrder.setClientId(order.getClient().getId());
        soapOrder.setClientName(order.getClient().getName());
        soapOrder.setOrderDate(convertToXMLGregorianCalendar(order.getOrderDate()));
        soapOrder.setStatus(OrderStatus.fromValue(order.getStatus().name()));
        soapOrder.setTotalAmount(order.getTotalAmount());
        soapOrder.setShippingAddress(order.getShippingAddress());
        return soapOrder;
    }

    private XMLGregorianCalendar convertToXMLGregorianCalendar(LocalDateTime dateTime) {
        try {
            GregorianCalendar gregorianCalendar = GregorianCalendar.from(
                    dateTime.atZone(ZoneId.systemDefault())
            );
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException("Error converting date", e);
        }
    }
}

