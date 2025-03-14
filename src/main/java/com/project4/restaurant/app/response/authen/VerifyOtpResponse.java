package com.project4.restaurant.app.response.authen;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerifyOtpResponse {
  private int status;
  private String message;
  private String data;
}
