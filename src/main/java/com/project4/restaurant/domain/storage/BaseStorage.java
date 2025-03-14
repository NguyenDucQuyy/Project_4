package com.project4.restaurant.domain.storage;

import com.project4.restaurant.domain.repository.*;
import com.project4.restaurant.domain.util.CacheKey;
import com.project4.restaurant.domain.util.RemoteCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class BaseStorage {
  @Autowired
  protected CacheKey cacheKey;
  @Autowired
  protected RemoteCache remoteCache;
  @Autowired
  protected UserAccountRepository userAccountRepository;
}