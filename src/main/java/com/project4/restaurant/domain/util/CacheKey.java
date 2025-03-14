package com.project4.restaurant.domain.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class CacheKey {

  public static final String NO_PREFIX_ID = "no_prefix_id";

  @Value("${cache.prefix-key}")
  private String cachePrefixKey;

  public String loginAttempts() {
    return cachePrefixKey + ":loginAttempts";
  }

  public String genListLanguageKey() {
    return cachePrefixKey + ":language:all";
  }

  public String genAccessTokenInfoPartnerKey(String partnerName) {
    return cachePrefixKey + ":" + partnerName + ":token_info";
  }

  public String genRefreshTokenKey(String username) {
    return cachePrefixKey + ":" + username;
  }

  public String genRegisterKey(String username) {
    return cachePrefixKey + ":user:register:" + username;
  }

  public String genOtpRegisterKey(String username) {
    return cachePrefixKey + ":user:otp:" + username;
  }

  public String genUserMetadataRegister(String username) {
    return cachePrefixKey + ":user:user_metadata:" + username;
  }

  //forgot-password
  public String gentUserForgotPasswordKey(String username) {
    return cachePrefixKey + ":user:forgot_password:" + username;
  }

  public String genOtpForgotPassword(String username) {
    return cachePrefixKey + ":user:forgot_password:otp:" + username;
  }

  public String genUserMetadataForgotPassword(String username) {
    return cachePrefixKey + ":user:forgot_password:user_metadata:" + username;
  }

  public String genTokenResetPassword(String username) {
    return cachePrefixKey + ":user:reset_password:token:" + username;
  }

  public String genConfig(String key) {
    return cachePrefixKey + ":config:" + key;
  }

  public String genCacheKey(String keyCache) {
    return keyCache;
  }

  public String genCacheUserDetailKey(String keyCache) {
    return cachePrefixKey + ":agency:" + keyCache;
  }

  public String genCacheKey(Object detailId, String keyCache) {
    return detailId + ":" + keyCache;
  }

  public String genPageFilterForEvict() {
    return "page:" + NO_PREFIX_ID + "*";
  }

  public String genPageFilterKey(Object filter, Pageable pageable) throws IOException, NoSuchAlgorithmException {
    return "*"; //genPageFilterKey(NO_PREFIX_ID, filter, pageable);
  }

  public String genPageFilterForEvict(Object prefixId) {
    return genFilterForEvict("page", prefixId);
  }

  public String genPageFilterKey(Object prefixId, Object filter, Pageable pageable) throws IOException, NoSuchAlgorithmException {
    return genFilterKey("page", prefixId, filter, pageable);
  }

  public String genSlideFilterForEvict(Object prefixId) {
    return genFilterForEvict("slide", prefixId);
  }

  public String genSlideFilterKey(Object prefixId, Object filter, Pageable pageable) throws IOException, NoSuchAlgorithmException {
    return genFilterKey("slide", prefixId, filter, pageable);
  }

  public String genUserNotificationCount(String username) {
    return cachePrefixKey + ":notification:count:" + username;
  }

  public String genFilterForEvict(String prefix, Object prefixId) {
    return  prefix + ":" + prefixId + "*";
  }

  public String genFilterKey(String prefix, Object prefixId, Object filter, Pageable pageable) throws IOException, NoSuchAlgorithmException {
    // Chuyển đổi đối tượng filter thành chuỗi JSON
    String filterJson = JsonParser.toJson(filter);

    // Tính toán hash MD5 của chuỗi JSON
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] hashBytes = md.digest(filterJson.getBytes());
    StringBuilder sb = new StringBuilder();
    sb.append(prefix);
    sb.append(":");
    sb.append(prefixId);
    sb.append(":");
    for (byte b : hashBytes) {
      sb.append(String.format("%02x", b));
    }
    sb.append("_").append(pageable.getPageNumber()).append("_").append(pageable.getPageSize());
    return sb.toString();
  }
}