package com.project4.restaurant.domain.storage.common;

import com.google.common.collect.Lists;
import com.project4.restaurant.domain.entity.common.Config;
import com.project4.restaurant.domain.pojo.TeleInfo;
import com.project4.restaurant.domain.repository.ConfigRepository;
import com.project4.restaurant.domain.storage.BaseStorage;
import com.project4.restaurant.domain.util.JsonParser;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Log4j2
public class ConfigStorage extends BaseStorage {
  private final ConfigRepository configRepository;

  public ConfigStorage(ConfigRepository configRepository) {
    this.configRepository = configRepository;
  }

//  public Page<Config> findAll(String key, Pageable pageable) {
//    return configRepository.findAll(configSpecification(key), pageable);
//  }

  public Page<Config> findAll(Query condition, Pageable pageable) {
    return configRepository.findAll(condition, pageable);
  }

  public Config findById(Integer id) {
    return configRepository.findById(id).orElse(null);
  }

  public void save(Config config) {
    configRepository.save(config);
  }

  public void deleteById(Integer id) {
    configRepository.deleteById(id);
  }

  public boolean existsById(Integer id) {
    return configRepository.existsById(id);
  }

  public String getValueConfigByKeyConfigCache(String keyConfig) {
    String key = cacheKey.genConfig(keyConfig);
    String valueConfig = remoteCache.get(key);
    if (valueConfig == null) {
      Config config = configRepository.findByKeyConfig(keyConfig);
      valueConfig = config == null ? null : config.getValueConfig();
      remoteCache.put(key, valueConfig, 60 * 60);//1 tieng
    }
    return valueConfig;
  }

  public int getMessageErrorNumber() {
    int number = 5;

    String valueConfig = this.getValueConfigByKeyConfigCache("message_error_number");
    if (valueConfig != null) {
      try {
        number = Integer.parseInt(valueConfig);
      } catch (Exception e) {
        log.error("==========>getMessageErrorNumber:exception:{}", e.getMessage());
      }
    }
    return number;
  }

  public Specification<Config> configSpecification(String keyConfig) {
    return (root, query, criteriaBuilder) -> {
      List<Predicate> conditionLists = new ArrayList<>();
      if (keyConfig != null) {
        conditionLists.add(criteriaBuilder.like(root.get("keyConfig"), "%" + keyConfig + "%"));
      }
      return criteriaBuilder.and(conditionLists.toArray(new Predicate[0]));
    };
  }

  public List<TeleInfo> getListTeleInfo() {
    List<TeleInfo> teleInfos;
    String value = this.getValueConfigByKeyConfigCache("tele_noti_otp");
    try {
      teleInfos = JsonParser.arrayList(value, TeleInfo.class);
    } catch (Exception e) {
      return Lists.newArrayList();
    }
    return teleInfos;
  }

  public boolean getEnableCheckBookingInfoByCustomsDeclarationPartner() {
    boolean enableCheckBookingInfoByCustomsDeclarationPartner = true;

    String valueConfig = this.getValueConfigByKeyConfigCache("check_booking_info_by_customs_declaration_partner");
    if (valueConfig != null) {
      try {
        enableCheckBookingInfoByCustomsDeclarationPartner = Boolean.parseBoolean(valueConfig);
      } catch (Exception e) {
        log.error("==========>getEnableCheckBookingInfoByCustomsDeclarationPartner:exception:{}", e.getMessage());
      }
    }
    return enableCheckBookingInfoByCustomsDeclarationPartner;
  }

  public boolean useApiVTP() {
    boolean useApiVTP = false;
    String valueConfig = this.getValueConfigByKeyConfigCache("use_api_vtp");
    if (valueConfig != null) {
      try {
        useApiVTP = Boolean.parseBoolean(valueConfig);
      } catch (Exception e) {
        log.error("==========>useApiVTP:exception:{}", e.getMessage());
      }
    }
    return useApiVTP;
  }

  public boolean enableSendTelegram() {
    boolean enableSendTelegram = false;
    String valueConfig = this.getValueConfigByKeyConfigCache("enable_send_telegram");
    if (valueConfig != null) {
      try {
        enableSendTelegram = Boolean.parseBoolean(valueConfig);
      } catch (Exception e) {
        log.error("==========>enableSendTelegram:exception:{}", e.getMessage());
      }
    }
    return enableSendTelegram;
  }

  public List<String> getListCustomsClearanceArea() {
    return Lists.newArrayList("Viettel Post");
  }

  public String getLinkSampleFile() {
    String link = "https://s3user10106.s3.cloudstorage.com.vn/evtp2-dev/2025/01/02/mau_import_nhiem_vu_1735801480390.xlsx";
    String valueConfig = this.getValueConfigByKeyConfigCache("link_sample_file");
    if (valueConfig != null) {
      link = valueConfig;
    }
    return link;
  }

  public int getTimeCancelPayment() {
    int minus = 15;

    String valueConfig = this.getValueConfigByKeyConfigCache("time_cancel_payment_minus");
    if (valueConfig != null) {
      try {
        minus = Integer.parseInt(valueConfig);
      } catch (Exception e) {
        log.error("==========>getTimeCancelPayment:exception:{}", e.getMessage());
      }
    }
    return minus;
  }

  public boolean useApiGenQRSimulator() {
    boolean enable = false;

    String valueConfig = this.getValueConfigByKeyConfigCache("use_api_gen_qr_simulator");
    if (valueConfig != null) {
      try {
        enable = Boolean.parseBoolean(valueConfig);
      } catch (Exception e) {
        log.error("==========>useApiGenQRSimulator:exception:{}", e.getMessage());
      }
    }
    return enable;
  }


}
