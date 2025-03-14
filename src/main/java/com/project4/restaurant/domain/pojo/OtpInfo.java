package com.project4.restaurant.domain.pojo;

import lombok.Data;

@Data
public class OtpInfo {
  private String to;
  private String otpCode;
  private Method method;
  private Type type;

  public enum Method {
    EMAIL,
    SMS,
    TELEGRAM
  }

  public enum Type {
    NEW_ACCOUNT,
    FORGET_PASSWORD
  }
}
