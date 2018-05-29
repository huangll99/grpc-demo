package com.hll.grpc.book.server;

import com.hll.grpc.book.server.config.TokenServerInterceptor;
import com.hll.grpc.book.server.service.AuthServiceImpl;
import com.hll.grpc.book.server.service.BookServiceImpl;
import io.grpc.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.io.IOException;

/**
 * Author: huangll
 * Written on 18/5/28.
 */
@SpringBootApplication
public class BookServer {

  public static void main(String[] args) throws IOException, InterruptedException {
    ConfigurableApplicationContext context = SpringApplication.run(BookServer.class, args);
    ConfigurableEnvironment environment = context.getEnvironment();
    Server server = ServerBuilder.forPort(environment.getProperty("book.server.port", Integer.class))
        .addService(context.getBean(BookServiceImpl.class))
        .addService(context.getBean(AuthServiceImpl.class))
        .intercept(context.getBean(TokenServerInterceptor.class))
        .build()
        .start();

    server.awaitTermination();
  }
}
