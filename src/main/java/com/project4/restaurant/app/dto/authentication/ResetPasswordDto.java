package com.project4.restaurant.app.dto.authentication;

import com.project4.restaurant.domain.core.annotation.NoWhiteSpace;
import com.project4.restaurant.domain.core.annotation.ValidEmail;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ResetPasswordDto {
  @NotNull(message = "Email không được để trống")
  @NotBlank(message = "Email không được để trống")
  @Size(max = 30, message = "Email tối đa 30 ký tự")
  @ValidEmail(message = "Email không hợp lệ")
  private String email;

  @NoWhiteSpace(message = "Mật khẩu không được chứa khoảng trắng")
  @NotBlank(message = "Mật khẩu vui lòng không để trống")
  @NotNull(message = "Mật khẩu vui lòng không để trống")
  @NotEmpty(message = "Mật khẩu vui lòng không để trống")
  //@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z0-9\\\\!@#$%^&*()_+={}\\[\\]|:;\"'<>.,?/~-]{6,}$", message = "Mật khẩu tối thiểu 6 ký tự và bao gồm cả chữ và số")
  @Size(min = 8, message = "Mật khẩu tối thiểu 8 ký tự")
  private String password;

  @NoWhiteSpace(message = "Mật khẩu không được chứa khoảng trắng")
  @NotBlank(message = "Mật khẩu vui lòng không để trống")
  @NotNull(message = "Mật khẩu vui lòng không để trống")
  @NotEmpty(message = "Mật khẩu vui lòng không để trống")
  //@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z0-9\\\\!@#$%^&*()_+={}\\[\\]|:;\"'<>.,?/~-]{6,}$", message = "Mật khẩu tối thiểu 6 ký tự và bao gồm cả chữ và số")
  @Size(min = 8, message = "Mật khẩu tối thiểu 8 ký tự")
  private String confirmPassword;

  @NotNull(message = "token đổi mật khẩu không được để trống")
  @NotBlank(message = "token đổi mật khẩu không được để trống")
  private String token;

  public void formatAllField() {
    if (this.email != null) {
      this.email = this.email.trim().toLowerCase();
      if (this.email.isBlank()) {
        this.email = null;
      }
    }
    if (this.password != null) {
      this.password = this.password.trim();
      if (this.password.isBlank()) {
        this.password = null;
      }
    }
    if (this.confirmPassword != null) {
      this.confirmPassword = this.confirmPassword.trim();
      if (this.confirmPassword.isBlank()) {
        this.confirmPassword = null;
      }
    }
    if (this.token != null) {
      this.token = this.token.trim();
      if (this.token.isBlank()) {
        this.token = null;
      }
    }
  }
}
