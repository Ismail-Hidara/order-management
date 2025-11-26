package com.example.order_management.grpc;

import com.example.notification.NotificationServiceGrpc;
import com.example.notification.OrderNotificationRequest;
import com.example.notification.OrderNotificationResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
@Slf4j
public class NotificationGrpcClient {

    @Value("${grpc.server.port:9090}")
    private int grpcPort;

    private ManagedChannel channel;
    private NotificationServiceGrpc.NotificationServiceBlockingStub blockingStub;

    @PostConstruct
    public void init() {
        channel = ManagedChannelBuilder.forAddress("localhost", grpcPort)
                .usePlaintext()
                .build();
        blockingStub = NotificationServiceGrpc.newBlockingStub(channel);
        log.info("gRPC client initialized for NotificationService on port {}", grpcPort);
    }

    @PreDestroy
    public void shutdown() {
        if (channel != null) {
            channel.shutdown();
            log.info("gRPC client channel shut down");
        }
    }

    public OrderNotificationResponse sendOrderNotification(
            Long orderId,
            Long clientId,
            String clientName,
            String clientEmail,
            Double totalAmount,
            String status,
            String message) {

        try {
            OrderNotificationRequest request = OrderNotificationRequest.newBuilder()
                    .setOrderId(orderId)
                    .setClientId(clientId)
                    .setClientName(clientName)
                    .setClientEmail(clientEmail)
                    .setTotalAmount(totalAmount)
                    .setStatus(status)
                    .setMessage(message)
                    .build();

            log.info("Sending notification via gRPC for order: {}", orderId);
            return blockingStub.sendOrderNotification(request);

        } catch (Exception e) {
            log.error("Error sending notification via gRPC", e);

            // Return failure response
            return OrderNotificationResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Failed to send notification: " + e.getMessage())
                    .build();
        }
    }
}

