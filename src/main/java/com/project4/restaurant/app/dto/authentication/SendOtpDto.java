package com.project4.restaurant.app.dto.authentication;

import lombok.Data;

@Data
public class SendOtpDto {
  private String function;
  private String otpType;
  private String account;
  private String type;
}
