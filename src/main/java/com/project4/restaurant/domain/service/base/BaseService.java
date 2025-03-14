package com.project4.restaurant.domain.service.base;

import com.google.common.collect.Lists;
import com.project4.restaurant.domain.ModelMapper;
import com.project4.restaurant.domain.entity.Sequence;
import com.project4.restaurant.domain.factory.ProcessOTPFactory;
import com.project4.restaurant.domain.storage.*;
import com.project4.restaurant.domain.service.LuaCacheService;
import com.project4.restaurant.domain.storage.common.ConfigStorage;
import com.project4.restaurant.domain.util.CacheKey;
import com.project4.restaurant.domain.util.MapperUtil;
import com.project4.restaurant.domain.util.RemoteCache;
import jakarta.validation.Validator;
import lombok.extern.log4j.Log4j2;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@Log4j2
public class BaseService {
  protected static final String BLOCK_TIME_KEY = "block_time";
  protected static final String FAILED_ATTEMPTS_KEY = "failed_attempts";

  protected static final int PARTNER_SUCCESS_CODE = 1;
  protected static final int FEIGN_FAIL_CODE = 500;
  protected static final String FORGOT_PASSWORD_OTP_FUNCTION_CODE = "RESTAURANT_FORGOT_PASSWORD";
  protected static final String REGISTER_OTP_FUNCTION_CODE = "RESTAURANT_REGISTER";
  protected static final String OTP_FUNCTION_TYPE = "EMAIL";
  protected static final String SEND_TYPE = "NUMBER";
  @Autowired protected ModelMapper modelMapper;
  @Autowired protected MongoOperations mongoOperations;
  @Autowired protected UserStorage userStorage;
  @Autowired protected ConfigStorage configStorage;
  @Autowired protected Validator validator;
  @Autowired protected RemoteCache remoteCache;
  @Autowired protected CacheKey cacheKey;
  @Autowired protected LuaCacheService luaCacheService;
  @Autowired protected RedissonClient redissonClient;
  @Autowired protected MapperUtil mapperUtil;
  @Autowired private ProcessOTPFactory processOTPFactory;

  public long generateSequence(String seqName) {
    Sequence counter =
        mongoOperations.findAndModify(
            Query.query(Criteria.where("_id").is(seqName)),
            new Update().inc("seq", 1),
            FindAndModifyOptions.options().returnNew(true).upsert(true),
            Sequence.class);
    return !Objects.isNull(counter) ? counter.getSeq() : 1;
  }

  public String processMessageError(List<String> messageError) {
    int maxMessageError = configStorage.getMessageErrorNumber();
    if (messageError.size() > maxMessageError) {
      messageError = messageError.subList(0, maxMessageError);
    }
    List<String> messageResult = Lists.newArrayList();
    for (String message : messageError) {
      messageResult.add(message);
    }
    return String.join("; ", messageResult);
  }

//  protected VerifyOtpResponse getVerifyOtpResponse(PartnerVerifyOtpDto verifyOtpDto) {
//    try {
//      VerifyOtpResponse response = processOTPFactory.verifyOTP(verifyOtpDto);
//      log.debug("==============>getVerifyOtpResponse= {}", JsonParser.toJson(response));
//      return response;
//    } catch (FeignException | IOException e) {
//      log.error("========>getVerifyOtpResponse: {}", e.toString());
//      return VerifyOtpResponse.builder().status(FEIGN_FAIL_CODE).message("Hệ thống bận, không thể gửi OTP").build();
//    }
//  }
}
