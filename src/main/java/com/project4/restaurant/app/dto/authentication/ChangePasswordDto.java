package com.project4.restaurant.app.dto.authentication;

import com.project4.restaurant.domain.core.annotation.NoWhiteSpace;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Data
public class ChangePasswordDto {
  @NoWhiteSpace(message = "Mật khẩu không được chứa khoảng trắng")
  @NotBlank(message = "Mật khẩu vui lòng không để trống")
  @NotNull(message = "Mật khẩu vui lòng không để trống")
  @NotEmpty(message = "Mật khẩu vui lòng không để trống")
  //@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z0-9\\\\!@#$%^&*()_+={}\\[\\]|:;\"'<>.,?/~-]{6,}$", message = "Mật khẩu tối thiểu 6 ký tự và bao gồm cả chữ và số")
  @Size(min = 8, message = "Mật khẩu tối thiểu 8 ký tự")
  public String oldPassword;

  @NoWhiteSpace(message = "Mật khẩu không được chứa khoảng trắng")
  @NotBlank(message = "Mật khẩu vui lòng không để trống")
  @NotNull(message = "Mật khẩu vui lòng không để trống")
  @NotEmpty(message = "Mật khẩu vui lòng không để trống")
  //@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z0-9\\\\!@#$%^&*()_+={}\\[\\]|:;\"'<>.,?/~-]{6,}$", message = "Mật khẩu tối thiểu 6 ký tự và bao gồm cả chữ và số")
  @Size(min = 8, message = "Mật khẩu tối thiểu 8 ký tự")
  public String newPassword;

  @NoWhiteSpace(message = "Mật khẩu không được chứa khoảng trắng")
  @NotBlank(message = "Mật khẩu vui lòng không để trống")
  @NotNull(message = "Mật khẩu vui lòng không để trống")
  @NotEmpty(message = "Mật khẩu vui lòng không để trống")
  //@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z0-9\\\\!@#$%^&*()_+={}\\[\\]|:;\"'<>.,?/~-]{6,}$", message = "Mật khẩu tối thiểu 6 ký tự và bao gồm cả chữ và số")
  @Size(min = 8, message = "Mật khẩu tối thiểu 8 ký tự")
  public String confirmPassword;
}
