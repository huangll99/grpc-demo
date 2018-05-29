package com.hll.grpc.book.client.grpc;

import com.hll.grpc.api.BookServiceGrpc;
import com.hll.grpc.api.auth.AuthServiceGrpc;
import com.hll.grpc.api.auth.AuthServiceProto;
import io.grpc.*;
import io.grpc.stub.MetadataUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Author: huangll
 * Written on 18/5/28.
 */
@Component
public class GrpcServerConfig {

  private ManagedChannel channel;

  private String token;

  public GrpcServerConfig() {
    this.channel = ManagedChannelBuilder.forAddress("localhost", 9999)
        // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
        // needing certificates.
        .usePlaintext()
        .intercept(new ClientInterceptor() {
          @Override
          public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
            Metadata metadata = new Metadata();
            if (token != null) {
              metadata.put(Metadata.Key.of("token", Metadata.ASCII_STRING_MARSHALLER), token);
            }
            return MetadataUtils.newAttachHeadersInterceptor(metadata).interceptCall(method, callOptions, next);
          }
        })
        .build();

    AuthServiceGrpc.AuthServiceBlockingStub authService = AuthServiceGrpc.newBlockingStub(channel);
    AuthServiceProto.Result result = authService.authenticate(
        AuthServiceProto.Credit.newBuilder().setUsername("huangll").setPassword("123456").build()
    );
    if (result.getSuccess()) {
      this.token = result.getToken();
    }
  }

  @Bean
  public BookServiceGrpc.BookServiceBlockingStub bookService() {
    return BookServiceGrpc.newBlockingStub(channel);
  }

  @Bean
  public AuthServiceGrpc.AuthServiceBlockingStub authService() {
    return AuthServiceGrpc.newBlockingStub(channel);
  }
}
