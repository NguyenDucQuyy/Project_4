package com.project4.restaurant.app.dto.authentication;

import com.project4.restaurant.domain.core.annotation.NoWhiteSpace;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LoginDto {
  @NoWhiteSpace(message = "Tên tài khoản không được chứa khoảng trắng")
  @NotBlank(message = "Tên tài khoản không được chứa khoảng trắng")
//  @NotNull(message = "Tên tài khoản vui lòng không để trống")
//  @NotEmpty(message = "Tên tài khoản vui lòng không để trống")
//  @Size(min = 5, message = "Tên tài khoản tối thiểu 5 ký tự")
//  @Size(max = 15, message = "Tên tài khoản tối đa 15 ký tự")
  //@Pattern(regexp = "^(?=.*[A-Za-z])[A-Za-z0-9!@#$%^&*()_+={}\\[\\]|:;\"'<>.,?/~-]*$", message = "Tên tài khoản bao gồm ít nhất 1 chữ số")
  private String username;

  @NoWhiteSpace(message = "Mật khẩu không được chứa khoảng trắng")
  @NotBlank(message = "Mật khẩu không được chứa khoảng trắng")
//  @NotNull(message = "Mật khẩu vui lòng không để trống")
//  @NotEmpty(message = "Mật khẩu vui lòng không để trống")
  //@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z0-9\\\\!@#$%^&*()_+={}\\[\\]|:;\"'<>.,?/~-]{6,}$", message = "Mật khẩu tối thiểu 6 ký tự và bao gồm cả chữ và số")
  //@Size(min = 8, message = "Mật khẩu tối thiểu 8 ký tự")
  private String password;

  private Boolean rememberMe = false;

  public void formatAllField() {
    if (this.username != null) {
      this.username = this.username.trim();
      if (this.username.isBlank()) {
        this.username = null;
      }
    }
    if (password != null) {
      this.password = this.password.replaceAll("\\s+", "");
      if (this.password.isBlank()) {
        this.password = null;
      }
    }
  }
}
