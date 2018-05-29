package com.hll.grpc.book.server.service;

import com.hll.grpc.api.auth.AuthServiceGrpc;
import com.hll.grpc.api.auth.AuthServiceProto;
import com.hll.grpc.book.server.config.TokenStore;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Author: huangll
 * Written on 18/5/29.
 */
@Component
public class AuthServiceImpl extends AuthServiceGrpc.AuthServiceImplBase {

  @Autowired
  private TokenStore tokenStore;

  @Override
  public void authenticate(AuthServiceProto.Credit credit, StreamObserver<AuthServiceProto.Result> responseObserver) {
    System.out.println(credit.getUsername());
    System.out.println(credit.getPassword());
    String username = credit.getUsername();
    String password = credit.getPassword();

    if ("huangll".equalsIgnoreCase(username) && "123456".equalsIgnoreCase(password)) {
      AuthServiceProto.Result result = AuthServiceProto.Result.newBuilder().setSuccess(true).setMsg("ok").setToken(generateToken(username)).build();
      responseObserver.onNext(result);
      tokenStore.put(generateToken(username), credit);
    } else {
      AuthServiceProto.Result result = AuthServiceProto.Result.newBuilder().setSuccess(false).setMsg("not authenticated").build();
      responseObserver.onNext(result);
    }

    responseObserver.onCompleted();
  }

  private String generateToken(String username) {
    return "xxx-" + username;
  }
}
