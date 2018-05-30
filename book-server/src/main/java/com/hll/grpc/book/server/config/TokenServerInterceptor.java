package com.hll.grpc.book.server.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import io.grpc.*;
import io.grpc.internal.NoopServerCall;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * Author: huangll
 * Written on 18/5/29.
 */
@Component
public class TokenServerInterceptor implements ServerInterceptor {


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
    Algorithm algorithm2;
    try {
      algorithm2 = Algorithm.HMAC256("SECRETs");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    JWTVerifier jwtVerifier = JWT.require(algorithm2)
        .withIssuer("gsafety")
        .build();
    try {
      jwtVerifier.verify(token);
    } catch (JWTVerificationException e) {
      return false;
    }
    return true;
  }
}
