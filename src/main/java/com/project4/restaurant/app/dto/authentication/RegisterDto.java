package com.project4.restaurant.app.dto.authentication;

import com.project4.restaurant.domain.core.annotation.NoWhiteSpace;
import com.project4.restaurant.domain.core.annotation.ValidEmail;
import com.project4.restaurant.domain.core.annotation.ValidPhoneNumber;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class RegisterDto {
  @NotBlank(message = "Họ và tên vui lòng không để trống")
  @NotNull(message = "Họ và tên vui lòng không để trống")
  @NotEmpty(message = "Họ và tên vui lòng không để trống")
  @Size(min = 5, message = "Họ và tên tối thiểu 5 ký tự")
  @Size(max = 100, message = "Họ và tên tối đa 100 ký tự")
  private String fullName;

  @NoWhiteSpace(message = "Tên tài khoản không được chứa khoảng trắng")
  @NotBlank(message = "Tên tài khoản vui lòng không để trống")
  @NotNull(message = "Tên tài khoản vui lòng không để trống")
  @NotEmpty(message = "Tên tài khoản vui lòng không để trống")
  @Size(min = 1, message = "Tên tài khoản tối thiểu 1 ký tự")
  @Size(max = 15, message = "Tên tài khoản tối đa 15 ký tự")
  //@Pattern(regexp = "^(?=.*[A-Za-z])[A-Za-z0-9!@#$%^&*()_+={}\\[\\]|:;\"'<>.,?/~-]*$", message = "Tên tài khoản không được bao gồm khoảng trắng")
  private String username;

  @NoWhiteSpace(message = "Mật khẩu không được chứa khoảng trắng")
  @NotBlank(message = "Mật khẩu vui lòng không để trống")
  @NotNull(message = "Mật khẩu vui lòng không để trống")
  @NotEmpty(message = "Mật khẩu vui lòng không để trống")
  //@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z0-9\\\\!@#$%^&*()_+={}\\[\\]|:;\"'<>.,?/~-]{6,}$", message = "Mật khẩu tối thiểu 6 ký tự và bao gồm cả chữ và số")
  @Size(min = 8, message = "Mật khẩu tối thiểu 8 ký tự")
  private String password;

  @NoWhiteSpace(message = "Số điện thoại không được chứa khoảng trắng")
  @ValidPhoneNumber
  private String phoneNumber;

  @NoWhiteSpace(message = "Email không được chứa khoảng trắng")
  @NotBlank(message = "Email vui lòng không để trống")
  @NotNull(message = "Email vui lòng không để trống")
  @Size(max = 30, message = "Email tối đa 30 ký tự")
  @ValidEmail(message = "Email chưa đúng định dạng")
  private String email;

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
