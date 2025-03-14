package com.project4.restaurant.app.dto.authentication;

import com.project4.restaurant.domain.core.annotation.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VerifyOtpDto {
  @NotBlank(message = "Email không được để trống")
  @Size(max = 50, message = "Email tối đa 50 ký tự")
  @ValidEmail(message = "Email không đúng định dạng")
  private String email;

  @Size(min = 6, max = 6, message = "Mã OTP gồm 6 ký tự")
  @NotBlank(message = "Mã OTP không được để trống")
  @NotNull(message = "Mã OTP không được để trống")
  private String otpCode;

  public void formatAllField() {
    if (this.email != null) {
      this.email = this.email.trim().toLowerCase();
      if (this.email.isBlank()) {
        this.email = null;
      }
    }
    if (this.otpCode != null) {
      this.otpCode = this.otpCode.trim();
      if (this.otpCode.isBlank()) {
        this.otpCode = null;
      }
    }
  }
}
