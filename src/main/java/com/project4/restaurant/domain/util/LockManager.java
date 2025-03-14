package com.project4.restaurant.domain.util;

import lombok.extern.log4j.Log4j2;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Log4j2
public class LockManager {
  public static final int WAIT_TIME = 5; // the maximum time to aquire the lock
  public static final int TIME_LOCK_IN_SECOND = 10;

  @Value("${cache.prefix-key}")
  private String prefixKey;

  private final RedissonClient client;

  public LockManager(RedissonClient client) {
    this.client = client;
  }

  protected String lockPrefixKey() {
    return prefixKey + ":lock";
  }

  public RLock startLockByPhoneNumber(String phoneNumber) {
    RLock lock = client.getLock(lockPrefixKey() + ":user:" + phoneNumber);
    lock.lock(TIME_LOCK_IN_SECOND, TimeUnit.SECONDS);
    return lock;
  }

  public void unLock(RLock lock) {
    if (lock != null) lock.unlockAsync();
  }

  public RLock lockBookingPayment(Long bookingId) {
    return client.getLock(lockPrefixKey() + ":booking:payment" + bookingId);
  }

  public RLock lockBooking(Long bookingId) {
    return client.getLock(lockPrefixKey() + ":booking:" + bookingId);
  }

  public RLock lockUserId(Integer userId) {
    return client.getLock(lockPrefixKey() + ":user:" + userId);
  }

  public RLock startLockToken(String accessToken) {
    RLock lock = client.getLock(lockPrefixKey() + ":token:" + Helper.md5Token(accessToken));
    lock.lock(TIME_LOCK_IN_SECOND, TimeUnit.SECONDS);
    return lock;
  }
}