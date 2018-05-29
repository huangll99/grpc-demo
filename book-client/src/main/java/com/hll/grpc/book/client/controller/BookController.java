package com.hll.grpc.book.client.controller;

import com.google.common.collect.Maps;
import com.hll.grpc.api.BookServiceGrpc;
import com.hll.grpc.api.BookServiceProto;

import com.hll.grpc.api.auth.AuthServiceGrpc;
import com.hll.grpc.api.auth.AuthServiceProto;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Author: huangll
 * Written on 18/5/28.
 */
@RestController
public class BookController {

  @Autowired
  private BookServiceGrpc.BookServiceBlockingStub bookService;

  @Autowired
  private AuthServiceGrpc.AuthServiceBlockingStub authService;

  @ApiOperation("添加图书")
  @PostMapping("/addBook")
  public Map<String, Object> addBook() {
    BookServiceProto.Book book = BookServiceProto.Book.newBuilder()
        .setName("微服务实战").setDesc("一本关于微服务的书").setCount(100).build();
    BookServiceProto.Result result = bookService.addBook(book);
    Iterator<BookServiceProto.Result> resultIterator = bookService.lotsOfReplies(book);
    while (resultIterator.hasNext()) {
      BookServiceProto.Result r = resultIterator.next();
      System.out.println(r.getSuccess());
      System.out.println(r.getMsg());
    }

    HashMap<String, Object> res = Maps.newHashMap();
    res.put("success", result.getSuccess());
    res.put("msg", result.getMsg());
    return res;
  }
}
