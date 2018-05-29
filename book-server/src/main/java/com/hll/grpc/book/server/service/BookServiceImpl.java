package com.hll.grpc.book.server.service;

import com.hll.grpc.api.BookServiceGrpc;
import com.hll.grpc.api.BookServiceProto;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Component;

/**
 * Author: huangll
 * Written on 18/5/28.
 */
@Component
public class BookServiceImpl extends BookServiceGrpc.BookServiceImplBase {

  @Override
  public void addBook(BookServiceProto.Book book, StreamObserver<BookServiceProto.Result> responseObserver) {
    System.out.println(book.getName());
    System.out.println(book.getDesc());
    System.out.println(book.getCount());

    BookServiceProto.Result result = BookServiceProto.Result.newBuilder().setSuccess(true).setMsg("ok").build();
    responseObserver.onNext(result);
    responseObserver.onCompleted();
  }

  @Override
  public void lotsOfReplies(BookServiceProto.Book book, StreamObserver<BookServiceProto.Result> responseObserver) {
    System.out.println(book.getName());
    System.out.println(book.getDesc());
    System.out.println(book.getCount());


    BookServiceProto.Result.Builder builder = BookServiceProto.Result.newBuilder();
    BookServiceProto.Result r1 = builder.setSuccess(true).setMsg("hehe").build();
    System.out.println(r1);
    responseObserver.onNext(r1);

    BookServiceProto.Result.Builder builder2 = BookServiceProto.Result.newBuilder();
    builder2.setSuccess(false);
    builder2.setMsg("jjj");
    BookServiceProto.Result r2 = builder2.build();
    System.out.println(r2.getSuccess());


    responseObserver.onNext(r2);
    responseObserver.onCompleted();
  }

  @Override
  public StreamObserver<BookServiceProto.Book> pingPong(StreamObserver<BookServiceProto.Result> responseObserver) {
    return new StreamObserver<BookServiceProto.Book>() {
      @Override
      public void onNext(BookServiceProto.Book book) {
        System.out.println(book);
        BookServiceProto.Result.Builder builder = BookServiceProto.Result.newBuilder();
        BookServiceProto.Result r1 = builder.setSuccess(true).setMsg("hehe").build();
        System.out.println(r1);
        responseObserver.onNext(r1);
      }

      @Override
      public void onError(Throwable throwable) {

      }

      @Override
      public void onCompleted() {
        responseObserver.onCompleted();
      }
    };
  }
}
