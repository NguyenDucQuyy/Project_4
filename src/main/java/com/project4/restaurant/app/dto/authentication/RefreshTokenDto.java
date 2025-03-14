package com.project4.restaurant.app.dto.authentication;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenDto {
  @NotBlank(message = "Dữ liệu không trống")
  String refreshToken;
}
