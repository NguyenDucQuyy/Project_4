package com.project4.restaurant.domain.util;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RemoteCache {

  public static final int CACHE_DURATION_DEFAULT = 3600; // 1 tieng
  public static final int CACHE_6H_DURATION = 3600 * 6; // 6 tieng
  public static final int CACHE_5MIN_DURATION = 60 * 5; // 5 min
  public static final int CACHE_1MIN_DURATION = 60; // 1 min
  public static final int CACHE_1W_DURATION = 3600 * 24 * 7; // 1 week
  public static final int CACHE_3M_DURATION = 3600 * 24 * 90; // 3 month
  public static final int CACHE_1DAY_DURATION = 3600 * 24; // 1 day

  private final RedisTemplate<String, String> redisTemplate;

  private final RedissonClient redissonClient;

  public RemoteCache(RedisTemplate<String, String> redisTemplate, @Qualifier("redisson") RedissonClient redissonClient) {
    this.redisTemplate = redisTemplate;
    this.redissonClient = redissonClient;
  }

  public void rDequePut(String key, Object value) {
    try {
      RDeque<Object> queue = redissonClient.getDeque(key);
      queue.addAsync(value);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  public void deleteKey(String key) {
    try {
      RDeque<Object> queue = redissonClient.getDeque(key);
      queue.delete();
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  public <T> T rDequeGetFirst(String key) {
    try {
      RDeque<T> queue = redissonClient.getDeque(key);
      return queue.pollFirst();
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return null;
  }

  public <K, V> Map<K, V> getMap(String key) {
    try {
      RMap<K, V> map = redissonClient.getMap(key);
      return map.readAllMap();
    } catch (Exception e) {
      return Collections.emptyMap();
    }
  }

  public <K, V> void putMap(String key, Map<K, V> map) {
    try {
      RMap<K, V> redisMap = redissonClient.getMap(key);
      redisMap.put((K) map.keySet(), (V) map.values());
    } catch (Exception e) {
      log.error("Error putting map for key {}: {}", key, e.getMessage());
    }
  }

  public <K, V> void putAllMap(String key, Map<K, V> map, int expireTime) {
    try {
      RMap<K, V> redisMap = redissonClient.getMap(key);
      redisMap.delete();
      redisMap.putAll(map);
      redisMap.expire(expireTime, TimeUnit.SECONDS);
    } catch (Exception e) {
      log.error("Error putting map for key {}: {}", key, e.getMessage());
    }
  }

  /**
   * @param key
   * @param value Tăng số lượng người dùng đăng nhập
   */
  public void pfAdd(String key, String value) {
    try {
      RHyperLogLog<String> hp = redissonClient.getHyperLogLog(key);
      hp.addAsync(value);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  /**
   * @param key
   * @param value Đếm số lượng người dùng đăng nhập
   */
  public long pfCount(String key, String value) {
    try {
      RHyperLogLog<String> hp = redissonClient.getHyperLogLog(key);
      return hp.count();
    } catch (Exception e) {
      log.error(e.getMessage());
      return 0;
    }
  }

  public void putExpireMillis(String key, Object value, long expireTime) {
    try {
      redisTemplate.opsForValue().set(key, JsonParser.toJson(value), expireTime, TimeUnit.MILLISECONDS);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  public void put(String key, String value, int expireTime) {
    redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.SECONDS);
  }

  public void put(String key, String value, long expireTimeSends) {
    redisTemplate.opsForValue().set(key, value, expireTimeSends, TimeUnit.SECONDS);
  }

  public void put(String key, Object object, int expireTime) {
    try {
      put(key, JsonParser.toJson(object), expireTime);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  public void put(String key, Object object) {
    try {
      put(key, JsonParser.toJson(object), CACHE_DURATION_DEFAULT);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  public Double addAndGetAtomicDouble(String key, double amount) {
    try {
      RAtomicDouble rAtomicDouble = redissonClient.getAtomicDouble(key);
      rAtomicDouble.expire(Duration.ofDays(2));
      return rAtomicDouble.addAndGet(amount);
    } catch (Exception e) {
      log.error(e.getMessage());
      return null;
    }
  }

  public Double getAtomicDouble(String key) {
    try {
      RAtomicDouble rAtomicDouble = redissonClient.getAtomicDouble(key);
      return rAtomicDouble.get();
    } catch (Exception e) {
      log.error(e.getMessage());
      return null;
    }
  }

  public Long addAndGetAtomicLong(String key, long amount, int expireTime) {
    try {
      RAtomicLong rAtomicLong = redissonClient.getAtomicLong(key);
      rAtomicLong.expireIfNotSet(Duration.ofSeconds(expireTime));
      return rAtomicLong.addAndGet(amount);
    } catch (Exception e) {
      log.error(e.getMessage());
      return 0L;
    }
  }

  public void put(String key, String value) {
    redisTemplate.opsForValue().set(key, value);
  }

  public String get(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  public <T> List<T> getList(String key, Class<T> tClass) {
    try {
      String value = redisTemplate.opsForValue().get(key);
      return JsonParser.arrayList(value, tClass);
    } catch (Exception e) {
      return null;
    }
  }

  public <T> Page<T> getPage(String key, Class<T> tClass) {
    try {
      String value = redisTemplate.opsForValue().get(key);
      return JsonParser.toPage(value, tClass);
    } catch (Exception e) {
      return null;
    }
  }

  public <T> T get(String key, Class<T> tClass) {
    try {
      String value = redisTemplate.opsForValue().get(key);
      return JsonParser.entity(value, tClass);
    } catch (Exception e) {
      return null;
    }
  }

  public Boolean exists(String key) {
    return redisTemplate.hasKey(key);
  }

  public void del(String key) {
    redisTemplate.delete(key);
  }

  public void del(List<String> keys) {
    redisTemplate.delete(keys);
  }

  public Set<String> keys(String pattern) {
    return redisTemplate.keys(pattern);
  }

  // set cache
  public boolean zAdd(String key, String value, Long score) {
    return Boolean.TRUE.equals(redisTemplate.opsForZSet().add(key, value, score));
  }

  public boolean zAdds(String key, Set<ZSetOperations.TypedTuple<String>> typedTuples) {
    redisTemplate.opsForZSet().add(key, typedTuples);
    return false;
  }

  public void zdel(String key, String value) {
    redisTemplate.opsForZSet().remove(key, value);
  }

  // get top
  public Set<ZSetOperations.TypedTuple<String>> reverseRangeWithScores(String key, Long topUser) {

    return redisTemplate.boundZSetOps(key).reverseRangeWithScores(0, topUser);
  }

  public Set<ZSetOperations.TypedTuple<String>> rangeWithScores(String key, Long from, Long topUser) {

    return redisTemplate.boundZSetOps(key).rangeWithScores(from, topUser);
  }

  public Set<String> rangeByScore(String key, Double topUser) {

    return redisTemplate.boundZSetOps(key).rangeByScore(topUser, Double.MAX_VALUE);
  }


  public Long zSize(String key) {
    return redisTemplate.boundZSetOps(key).size();
  }

  // getRank
  public Long zRank(String key, String value) {
    return redisTemplate.boundZSetOps(key).reverseRank(value);
  }

  public Double zUpdatePoint(String key, String value, double score) {
    return redisTemplate.opsForZSet().incrementScore(key, value, score);
  }

  public Double score(String key, String value) {
    return redisTemplate.opsForZSet().score(key, value);
  }

  /**
   * for Millisecond
   *
   * @param key
   * @param value
   * @param expireTime
   */
  public void setInMs(String key, String value, long expireTime) {
    redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.MILLISECONDS);
  }

  public void setInMinute(String key, String value, long expireTime) {
    redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.MINUTES);
  }

  public Set<ZSetOperations.TypedTuple<String>> reverseRangeWithScoresCursor(String key, Integer to, long topUser) {

    return redisTemplate.boundZSetOps(key).reverseRangeWithScores(to, topUser);
  }

  public void set(String key, String value, int expireTime) {
    redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.SECONDS);
  }

  public RMapCache<String, Integer> getMapCache(String cacheKey) {
    return redissonClient.getMapCache(cacheKey);
  }
}