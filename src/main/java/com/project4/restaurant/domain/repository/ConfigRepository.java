package com.project4.restaurant.domain.repository;

import com.project4.restaurant.domain.entity.common.Config;
import com.project4.restaurant.domain.repository.base.MongoResourceRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigRepository extends MongoResourceRepository<Config, Integer> {
  Config findByKeyConfig(String key);
}
