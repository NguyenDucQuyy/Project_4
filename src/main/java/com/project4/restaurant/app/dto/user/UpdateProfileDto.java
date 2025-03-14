package com.project4.restaurant.app.dto.user;

import com.project4.restaurant.domain.core.annotation.NoWhiteSpace;
import com.project4.restaurant.domain.core.annotation.ValidEmail;
import com.project4.restaurant.domain.core.annotation.ValidPhoneNumber;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Data
public class UpdateProfileDto {
  @NotBlank(message = "Họ và tên vui lòng không để trống")
  @NotNull(message = "Họ và tên vui lòng không để trống")
  @NotEmpty(message = "Họ và tên vui lòng không để trống")
  @Size(min = 5, message = "Họ và tên tối thiểu 3 ký tự")
  @Size(max = 100, message = "Họ và tên tối đa 100 ký tự")
  private String fullName;

//  @NotBlank(message = "Tên tài khoản vui lòng không để trống")
//  @NotNull(message = "Tên tài khoản vui lòng không để trống")
//  @NotEmpty(message = "Tên tài khoản vui lòng không để trống")
//  @Size(min = 5, message = "Tên tài khoản tối thiểu 5 ký tự")
//  @Size(max = 15, message = "Tên tài khoản tối đa 15 ký tự")
//  @Pattern(regexp = "^(?=.*[A-Za-z])[A-Za-z0-9!@#$%^&*()_+={}\\[\\]|:;\"'<>.,?/~-]*$", message = "Tên tài khoản bao gồm ít nhất 1 chữ số")
//  private String username;

//  @NotNull(message = "Vui lòng chọn")
//  @Enumerated(EnumType.STRING)
//  private UserPosition position;

//  @NotNull(message = "Vui lòng chọn")
//  @NotEmpty(message = "Vui lòng chọn")
//  private List<DepartmentDto> departments;

  @NoWhiteSpace(message = "Email không được chứa khoảng trắng")
  @NotBlank(message = "Email vui lòng không để trống")
  @NotNull(message = "Email vui lòng không để trống")
  @Size(max = 30, message = "Email tối đa 30 ký tự")
  @ValidEmail(message = "Email chưa đúng định dạng")
  private String email;

  @NoWhiteSpace(message = "Số điện thoại không được chứa khoảng trắng")
  @ValidPhoneNumber
  private String phoneNumber;
  //private String imageUrl;
}
