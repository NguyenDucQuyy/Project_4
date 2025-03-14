package com.project4.restaurant.domain.factory;

import com.project4.restaurant.app.dto.authentication.SendOtpDto;
import com.project4.restaurant.app.response.authen.SendOtpResponse;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "processOTPFactory", url = "${otp.service-url}")
public interface ProcessOTPFactory {
  @Headers({"Content-Type: application/json", "Authorization: {token}"})
  @RequestLine("POST /api/otp/v2/sendOTP")
  SendOtpResponse sendOTP(
      @Param("token") String token,
      SendOtpDto request);

//  @Headers({"Content-Type: application/json"})
//  @RequestLine("POST /api/otp/verifyOTPV2")
  //VerifyOtpResponse verifyOTP(PartnerVerifyOtpDto request);
}
