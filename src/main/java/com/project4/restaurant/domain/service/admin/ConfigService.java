package com.project4.restaurant.domain.service.admin;

import com.project4.restaurant.app.dto.admin.ConfigCreateDto;
import com.project4.restaurant.app.dto.admin.ConfigUpdateDto;
import com.project4.restaurant.app.response.admin.ConfigResponse;
import com.project4.restaurant.domain.core.exception.BadRequestException;
import com.project4.restaurant.domain.entity.common.Config;
import com.project4.restaurant.domain.service.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfigService extends BaseService {
  @Autowired
  @Lazy
  private ConfigService self;

  public Page<ConfigResponse> findAll(Pageable pageable) {
    Page<Config> configPage = configStorage.findAll(null, pageable);
    return modelMapper.toPageConfigResponse(configPage);
  }

  public ConfigResponse getDetail(Integer id) {
    Config config = configStorage.findById(id);
    if (config == null) {
      throw new BadRequestException("Config " + id + " không tồn tại");
    }
    return modelMapper.toConfigResponse(config);
  }

  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
  public void create(ConfigCreateDto dto) {
    Config config = modelMapper.toConfig(dto);
    configStorage.save(config);
  }

  public void update(Integer id, ConfigUpdateDto dto) {
    if (!configStorage.existsById(id)) {
      throw new BadRequestException("Config " + id + " không tồn tại");
    }

    Config config = self.processUpdate(id, dto);

    self.processCache(config.getKeyConfig());
  }

  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
  public Config processUpdate(Integer id, ConfigUpdateDto dto) {
    Config config = configStorage.findById(id);
    if (config == null) {
      throw new BadRequestException("Config " + id + " không tồn tại");
    }

    config.setValueConfig(dto.getValueConfig());

    configStorage.save(config);
    return config;
  }

  public void delete(Integer id) {
    if (!configStorage.existsById(id)) {
      throw new BadRequestException("Config " + id + " không tồn tại");
    }

    Config config = self.processDelete(id);
    self.processCache(config.getKeyConfig());
  }

  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
  public Config processDelete(Integer id) {
    Config config = configStorage.findById(id);
    if (config == null) {
      throw new BadRequestException("Config " + id + " không tồn tại");
    }

    configStorage.deleteById(id);

    return config;
  }

  public void processCache(String key) {
    remoteCache.del(cacheKey.genConfig(key));
  }

}
