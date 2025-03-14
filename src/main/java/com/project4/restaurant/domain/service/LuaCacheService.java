package com.project4.restaurant.domain.service;

import lombok.extern.log4j.Log4j2;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.LongCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Log4j2
public class LuaCacheService {
  @Value("${cache.prefix-key}")
  private String cachePrefixKey;

  private final RedissonClient redissonClient;
  private final String batchEvictScript;
  private static final int BATCH_SIZE = 1000;

  public LuaCacheService(RedissonClient redissonClient, String batchEvictScript) {
    this.redissonClient = redissonClient;
    this.batchEvictScript = batchEvictScript;
  }
  /**
   * Xóa cache theo pattern bằng SCAN (batch processing)
   */
  public long evictByPatternBatch(String cacheName, String pattern) {
    String fullPattern = cachePrefixKey + ":" + cacheName + "::" + pattern;
    RScript script = redissonClient.getScript(LongCodec.INSTANCE);
    try {
      long deletedCount = script.eval(
          RScript.Mode.READ_WRITE,
          batchEvictScript, // Lua script trực tiếp trong code
          RScript.ReturnType.INTEGER,
          Collections.emptyList(), // Không cần key list
          fullPattern,             // Pattern dạng ARGV[1]
          BATCH_SIZE // Batch size dạng ARGV[2]
      );
      // Debugging: Check the debug output in Redis
      log.debug("Deleted {} keys matching pattern: {}", deletedCount, fullPattern);
      return deletedCount;
    } catch (Exception e) {
      log.error("Error during batch eviction: {}", e.getMessage(), e);
      throw e;
    }
  }
}