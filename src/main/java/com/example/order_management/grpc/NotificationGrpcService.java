package com.example.order_management.grpc;

import com.example.notification.*;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@GrpcService
@Slf4j
public class NotificationGrpcService extends NotificationServiceGrpc.NotificationServiceImplBase {

    // In-memory storage for notification history (in production, use a database)
    private final Map<Long, List<NotificationRecord>> notificationHistory = new ConcurrentHashMap<>();

    @Override
    public void sendOrderNotification(OrderNotificationRequest request,
                                     StreamObserver<OrderNotificationResponse> responseObserver) {

        log.info("Received notification request for order ID: {} and client: {}",
                request.getOrderId(), request.getClientName());

        try {
            // Simulate sending notification (email, SMS, push notification, etc.)
            String notificationId = UUID.randomUUID().toString();
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            // Store notification in history
            NotificationRecord record = NotificationRecord.newBuilder()
                    .setNotificationId(notificationId)
                    .setOrderId(request.getOrderId())
                    .setMessage(request.getMessage())
                    .setTimestamp(timestamp)
                    .setStatus("SENT")
                    .build();

            notificationHistory.computeIfAbsent(request.getClientId(), k -> new ArrayList<>())
                    .add(record);

            log.info("Notification sent successfully. ID: {}", notificationId);

            // Build response
            OrderNotificationResponse response = OrderNotificationResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Notification sent successfully to " + request.getClientEmail())
                    .setNotificationId(notificationId)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("Error sending notification", e);

            OrderNotificationResponse response = OrderNotificationResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Failed to send notification: " + e.getMessage())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void getNotificationHistory(NotificationHistoryRequest request,
                                       StreamObserver<NotificationHistoryResponse> responseObserver) {

        log.info("Fetching notification history for client ID: {}", request.getClientId());

        List<NotificationRecord> records = notificationHistory.getOrDefault(
                request.getClientId(), new ArrayList<>());

        // Apply limit if specified
        int limit = request.getLimit() > 0 ? request.getLimit() : records.size();
        List<NotificationRecord> limitedRecords = records.subList(
                Math.max(0, records.size() - limit), records.size());

        NotificationHistoryResponse response = NotificationHistoryResponse.newBuilder()
                .addAllNotifications(limitedRecords)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}

