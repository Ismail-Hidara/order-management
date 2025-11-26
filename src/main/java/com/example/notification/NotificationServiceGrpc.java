package com.example.notification;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * Notification Service
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.62.2)",
    comments = "Source: notification.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class NotificationServiceGrpc {

  private NotificationServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "notification.NotificationService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.example.notification.OrderNotificationRequest,
      com.example.notification.OrderNotificationResponse> getSendOrderNotificationMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendOrderNotification",
      requestType = com.example.notification.OrderNotificationRequest.class,
      responseType = com.example.notification.OrderNotificationResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.notification.OrderNotificationRequest,
      com.example.notification.OrderNotificationResponse> getSendOrderNotificationMethod() {
    io.grpc.MethodDescriptor<com.example.notification.OrderNotificationRequest, com.example.notification.OrderNotificationResponse> getSendOrderNotificationMethod;
    if ((getSendOrderNotificationMethod = NotificationServiceGrpc.getSendOrderNotificationMethod) == null) {
      synchronized (NotificationServiceGrpc.class) {
        if ((getSendOrderNotificationMethod = NotificationServiceGrpc.getSendOrderNotificationMethod) == null) {
          NotificationServiceGrpc.getSendOrderNotificationMethod = getSendOrderNotificationMethod =
              io.grpc.MethodDescriptor.<com.example.notification.OrderNotificationRequest, com.example.notification.OrderNotificationResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SendOrderNotification"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.notification.OrderNotificationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.notification.OrderNotificationResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NotificationServiceMethodDescriptorSupplier("SendOrderNotification"))
              .build();
        }
      }
    }
    return getSendOrderNotificationMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.notification.NotificationHistoryRequest,
      com.example.notification.NotificationHistoryResponse> getGetNotificationHistoryMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetNotificationHistory",
      requestType = com.example.notification.NotificationHistoryRequest.class,
      responseType = com.example.notification.NotificationHistoryResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.notification.NotificationHistoryRequest,
      com.example.notification.NotificationHistoryResponse> getGetNotificationHistoryMethod() {
    io.grpc.MethodDescriptor<com.example.notification.NotificationHistoryRequest, com.example.notification.NotificationHistoryResponse> getGetNotificationHistoryMethod;
    if ((getGetNotificationHistoryMethod = NotificationServiceGrpc.getGetNotificationHistoryMethod) == null) {
      synchronized (NotificationServiceGrpc.class) {
        if ((getGetNotificationHistoryMethod = NotificationServiceGrpc.getGetNotificationHistoryMethod) == null) {
          NotificationServiceGrpc.getGetNotificationHistoryMethod = getGetNotificationHistoryMethod =
              io.grpc.MethodDescriptor.<com.example.notification.NotificationHistoryRequest, com.example.notification.NotificationHistoryResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetNotificationHistory"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.notification.NotificationHistoryRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.notification.NotificationHistoryResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NotificationServiceMethodDescriptorSupplier("GetNotificationHistory"))
              .build();
        }
      }
    }
    return getGetNotificationHistoryMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static NotificationServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NotificationServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NotificationServiceStub>() {
        @java.lang.Override
        public NotificationServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NotificationServiceStub(channel, callOptions);
        }
      };
    return NotificationServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static NotificationServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NotificationServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NotificationServiceBlockingStub>() {
        @java.lang.Override
        public NotificationServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NotificationServiceBlockingStub(channel, callOptions);
        }
      };
    return NotificationServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static NotificationServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NotificationServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NotificationServiceFutureStub>() {
        @java.lang.Override
        public NotificationServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NotificationServiceFutureStub(channel, callOptions);
        }
      };
    return NotificationServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * Notification Service
   * </pre>
   */
  public interface AsyncService {

    /**
     * <pre>
     * Send order notification
     * </pre>
     */
    default void sendOrderNotification(com.example.notification.OrderNotificationRequest request,
        io.grpc.stub.StreamObserver<com.example.notification.OrderNotificationResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSendOrderNotificationMethod(), responseObserver);
    }

    /**
     * <pre>
     * Get notification history
     * </pre>
     */
    default void getNotificationHistory(com.example.notification.NotificationHistoryRequest request,
        io.grpc.stub.StreamObserver<com.example.notification.NotificationHistoryResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetNotificationHistoryMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service NotificationService.
   * <pre>
   * Notification Service
   * </pre>
   */
  public static abstract class NotificationServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return NotificationServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service NotificationService.
   * <pre>
   * Notification Service
   * </pre>
   */
  public static final class NotificationServiceStub
      extends io.grpc.stub.AbstractAsyncStub<NotificationServiceStub> {
    private NotificationServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NotificationServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NotificationServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Send order notification
     * </pre>
     */
    public void sendOrderNotification(com.example.notification.OrderNotificationRequest request,
        io.grpc.stub.StreamObserver<com.example.notification.OrderNotificationResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSendOrderNotificationMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Get notification history
     * </pre>
     */
    public void getNotificationHistory(com.example.notification.NotificationHistoryRequest request,
        io.grpc.stub.StreamObserver<com.example.notification.NotificationHistoryResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetNotificationHistoryMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service NotificationService.
   * <pre>
   * Notification Service
   * </pre>
   */
  public static final class NotificationServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<NotificationServiceBlockingStub> {
    private NotificationServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NotificationServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NotificationServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Send order notification
     * </pre>
     */
    public com.example.notification.OrderNotificationResponse sendOrderNotification(com.example.notification.OrderNotificationRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSendOrderNotificationMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Get notification history
     * </pre>
     */
    public com.example.notification.NotificationHistoryResponse getNotificationHistory(com.example.notification.NotificationHistoryRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetNotificationHistoryMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service NotificationService.
   * <pre>
   * Notification Service
   * </pre>
   */
  public static final class NotificationServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<NotificationServiceFutureStub> {
    private NotificationServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NotificationServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NotificationServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Send order notification
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.notification.OrderNotificationResponse> sendOrderNotification(
        com.example.notification.OrderNotificationRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSendOrderNotificationMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Get notification history
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.notification.NotificationHistoryResponse> getNotificationHistory(
        com.example.notification.NotificationHistoryRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetNotificationHistoryMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SEND_ORDER_NOTIFICATION = 0;
  private static final int METHODID_GET_NOTIFICATION_HISTORY = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SEND_ORDER_NOTIFICATION:
          serviceImpl.sendOrderNotification((com.example.notification.OrderNotificationRequest) request,
              (io.grpc.stub.StreamObserver<com.example.notification.OrderNotificationResponse>) responseObserver);
          break;
        case METHODID_GET_NOTIFICATION_HISTORY:
          serviceImpl.getNotificationHistory((com.example.notification.NotificationHistoryRequest) request,
              (io.grpc.stub.StreamObserver<com.example.notification.NotificationHistoryResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getSendOrderNotificationMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.notification.OrderNotificationRequest,
              com.example.notification.OrderNotificationResponse>(
                service, METHODID_SEND_ORDER_NOTIFICATION)))
        .addMethod(
          getGetNotificationHistoryMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.notification.NotificationHistoryRequest,
              com.example.notification.NotificationHistoryResponse>(
                service, METHODID_GET_NOTIFICATION_HISTORY)))
        .build();
  }

  private static abstract class NotificationServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    NotificationServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.example.notification.NotificationProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("NotificationService");
    }
  }

  private static final class NotificationServiceFileDescriptorSupplier
      extends NotificationServiceBaseDescriptorSupplier {
    NotificationServiceFileDescriptorSupplier() {}
  }

  private static final class NotificationServiceMethodDescriptorSupplier
      extends NotificationServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    NotificationServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (NotificationServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new NotificationServiceFileDescriptorSupplier())
              .addMethod(getSendOrderNotificationMethod())
              .addMethod(getGetNotificationHistoryMethod())
              .build();
        }
      }
    }
    return result;
  }
}
