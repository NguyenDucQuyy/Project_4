package com.project4.restaurant.app.dto.authentication;

import com.project4.restaurant.domain.core.annotation.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ForgotPasswordDto {
  @NotBlank(message = "Email không được để trống")
  @Size(max = 30, message = "Email tối đa 30 ký tự")
  @ValidEmail(message = "Email không đúng định dạng")
  private String email;

  public void formatAllField() {
    if (this.email != null) {
      this.email = this.email.trim().toLowerCase();
      if (this.email.isBlank()) {
        this.email = null;
      }
    }
  }
}
