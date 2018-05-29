package com.hll.grpc.book.server.config;

import io.grpc.*;
import io.grpc.internal.NoopServerCall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Author: huangll
 * Written on 18/5/29.
 */
@Component
public class TokenServerInterceptor implements ServerInterceptor {

  @Autowired
  private TokenStore tokenStore;

  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
    String fullMethodName = call.getMethodDescriptor().getFullMethodName();
    if (Const.TOKEN_ENDPOINT.equals(fullMethodName)) {
      return next.startCall(call, headers);
    }

    String token = headers.get(Metadata.Key.of("token", Metadata.ASCII_STRING_MARSHALLER));
    if (!checkToken(token)) {
      call.close(Status.PERMISSION_DENIED, headers);
      return new NoopServerCall.NoopServerCallListener<>();
    }

    return next.startCall(call, headers);
  }

  private boolean checkToken(String token) {
    return tokenStore.get(token) != null;
  }
}
