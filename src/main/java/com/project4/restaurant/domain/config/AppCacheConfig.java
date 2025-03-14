package com.project4.restaurant.domain.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.project4.restaurant.domain.Constants;
import com.project4.restaurant.domain.config.json.CustomJsonJacksonCodec;
import org.redisson.api.RedissonClient;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class AppCacheConfig implements CachingConfigurer {

  @Value("${cache.prefix-key}")
  private String cachePrefixKey;

  @Bean
  public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redissonClient) {
    return new RedissonConnectionFactory(redissonClient);
  }

  @Bean
  public Map<String, RedisCacheConfiguration> cacheConfig() {
    Map<String, RedisCacheConfiguration> config = new HashMap<>();

    // Cấu hình cho từng cache riêng biệt
    config.put(Constants.LANGUAGE_LIST_CACHE, cacheConfiguration().entryTtl(Duration.ofDays(1)));
    config.put(Constants.LANGUAGE_CACHE, cacheConfiguration().entryTtl(Duration.ofDays(1)));
    config.put(Constants.PROFILE_CACHE, cacheConfiguration().entryTtl(Duration.ofDays(1)));
    config.put(Constants.CUSTOMS_DEPARTMENT_LIST_CACHE, cacheConfiguration().entryTtl(Duration.ofDays(1)));
    config.put(Constants.GOODS_LIST_CACHE, cacheConfiguration().entryTtl(Duration.ofDays(1)));
    config.put(Constants.VEHICLE_LOAD_CAPACITY_LIST_CACHE, cacheConfiguration().entryTtl(Duration.ofDays(1)));
    config.put(Constants.MEASURE_UNIT_LIST_CACHE, cacheConfiguration().entryTtl(Duration.ofDays(1)));
    config.put(Constants.DRIVER_CONTACT_LIST_CACHE,cacheConfiguration().entryTtl(Duration.ofDays(1)));
    config.put(Constants.DRIVER_CONTACT_CACHE, cacheConfiguration().entryTtl(Duration.ofDays(1)));
    config.put(Constants.BOOKING_LIST_CACHE,cacheConfiguration().entryTtl(Duration.ofHours(1)));
    config.put(Constants.BOOKING_CACHE, cacheConfiguration().entryTtl(Duration.ofHours(1)));
    config.put(Constants.BOOKING_HISTORY_CACHE, cacheConfiguration().entryTtl(Duration.ofHours(1)));
    config.put(Constants.BOOKING_HISTORY_STATE_PARTNER_CACHE, cacheConfiguration().entryTtl(Duration.ofHours(1)));
    config.put(Constants.PROCEDURE_OFFICER_CONTACT_LIST_CACHE, cacheConfiguration().entryTtl(Duration.ofDays(1)));
    config.put(Constants.PROCEDURE_OFFICER_CONTACT_CACHE, cacheConfiguration().entryTtl(Duration.ofDays(1)));
    //    trip
    config.put(Constants.TRIP_STATISTICS_CACHE, cacheConfiguration().entryTtl(Duration.ofHours(2)));
    config.put(Constants.RUNNING_TRIP_CACHE, cacheConfiguration().entryTtl(Duration.ofHours(2)));
    config.put(Constants.COMMAND_TRIP_CACHE, cacheConfiguration().entryTtl(Duration.ofHours(2)));
    config.put(Constants.HISTORY_TRIP_CACHE, cacheConfiguration().entryTtl(Duration.ofHours(2)));
    config.put(Constants.TRIP_NOT_RECEIVE_CACHE, cacheConfiguration().entryTtl(Duration.ofHours(2)));
    config.put(Constants.TRIP_RECEIVED_CACHE, cacheConfiguration().entryTtl(Duration.ofHours(2)));
    config.put(Constants.TRIP_SEARCH_CACHE, cacheConfiguration().entryTtl(Duration.ofHours(2)));

//    agency
    config.put(Constants.COMMON_AGENCY_LIST_CACHE, cacheConfiguration().entryTtl(Duration.ofHours(2)));

    //payment
    config.put(Constants.PAYMENT_LIST_CACHE, cacheConfiguration().entryTtl(Duration.ofHours(2)));
    config.put(Constants.PAYMENT_DETAIL_CACHE, cacheConfiguration().entryTtl(Duration.ofHours(2)));

    return config;
  }

  @Bean
  public RedisCacheConfiguration cacheConfiguration() {
    ObjectMapper objectMapper = CustomJsonJacksonCodec.objectMapper();  // For @class retention
    objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL); // Keep @class info

    return RedisCacheConfiguration.defaultCacheConfig()
        .prefixCacheNameWith(cachePrefixKey + ":")
        .entryTtl(Duration.ofMinutes(30)) // Default TTL 30 minutes
        .disableCachingNullValues()
        .serializeKeysWith(
            RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)));
  }

  @Bean
  @Primary
  public RedisCacheManager cacheManager(RedissonConnectionFactory connectionFactory) {
    return RedisCacheManager.builder(connectionFactory)
        .cacheDefaults(cacheConfiguration())
        .withInitialCacheConfigurations(cacheConfig())
        .build();
  }

  @Bean
  public CacheManager caffeineCacheManager() {
    CaffeineCacheManager cacheManager = new CaffeineCacheManager();
    cacheManager.setCaffeine(Caffeine.newBuilder()
        .expireAfterWrite(60, TimeUnit.SECONDS)
        .maximumSize(300));
    return cacheManager;
  }

  @Override
  public CacheErrorHandler errorHandler() {
    return new SimpleCacheErrorHandler();
  }


  @Bean
  public String batchEvictScript() {
    // Script Lua sử dụng SCAN
    return "redis.replicate_commands() " +
        "local cursor = '0' " +
        "local count = 0 " +
        "local batchSize = tonumber(ARGV[2]) " +
        "if not batchSize then " +
        "    return redis.error_reply('Invalid batch size') " +
        "end " +
        "repeat " +
        "    local result = redis.call('SCAN', cursor, 'MATCH', ARGV[1], 'COUNT', batchSize) " +
        "    cursor = result[1] " +
        "    local keys = result[2] " +
        "    if #keys > 0 then " +
        "        for _, key in ipairs(keys) do " +
        "            redis.call('DEL', key) " +
        "            count = count + 1 " +
        "        end " +
        "    end " +
        "until cursor == '0' " +
        "return count";
  }
}
