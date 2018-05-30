package com.hll.grpc.book.server.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheStats;
import com.hll.grpc.api.auth.AuthServiceProto;

import java.util.concurrent.TimeUnit;

/**
 * Author: huangll
 * Written on 18/5/29.
 */
public class TokenStore {

  private Cache<String, AuthServiceProto.Credit> cache;

  public TokenStore() {
    cache = CacheBuilder.newBuilder().expireAfterWrite(6, TimeUnit.HOURS).build();
  }

  public void put(String token, AuthServiceProto.Credit credit) {
    cache.put(token, credit);
    CacheStats stats = cache.stats();
  }

  public AuthServiceProto.Credit get(String token) {
    return cache.getIfPresent(token);
  }
}
