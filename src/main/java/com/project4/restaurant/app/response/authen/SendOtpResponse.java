package com.project4.restaurant.app.response.authen;

import lombok.Data;

@Data
public class SendOtpResponse {
  private int status;
  private String message;
  private Data data;

  @lombok.Data
  public static class Data {
    private long timeResend;
  }
}
