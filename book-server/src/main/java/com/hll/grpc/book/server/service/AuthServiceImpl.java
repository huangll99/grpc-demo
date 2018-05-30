package com.hll.grpc.book.server.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.hll.grpc.api.auth.AuthServiceGrpc;
import com.hll.grpc.api.auth.AuthServiceProto;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * Author: huangll
 * Written on 18/5/29.
 */
@Component
public class AuthServiceImpl extends AuthServiceGrpc.AuthServiceImplBase {

  @Override
  public void authenticate(AuthServiceProto.Credit credit, StreamObserver<AuthServiceProto.Result> responseObserver) {
    System.out.println(credit.getUsername());
    System.out.println(credit.getPassword());
    String username = credit.getUsername();
    String password = credit.getPassword();

    if ("huangll".equalsIgnoreCase(username) && "123456".equalsIgnoreCase(password)) {
      AuthServiceProto.Result result = AuthServiceProto.Result.newBuilder().setSuccess(true).setMsg("ok").setToken(generateToken(username)).build();
      responseObserver.onNext(result);
    } else {
      AuthServiceProto.Result result = AuthServiceProto.Result.newBuilder().setSuccess(false).setMsg("not authenticated").build();
      responseObserver.onNext(result);
    }

    responseObserver.onCompleted();
  }

  private String generateToken(String username) {
    Algorithm algorithm;
    try {
      algorithm = Algorithm.HMAC256("SECRETs");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    return JWT.create()
        .withIssuer("gsafety")
        .withSubject("huangll")
        .withClaim("name", username)
        .sign(algorithm);
  }
}
