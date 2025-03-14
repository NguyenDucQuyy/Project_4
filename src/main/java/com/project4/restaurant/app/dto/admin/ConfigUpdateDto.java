package com.project4.restaurant.app.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConfigUpdateDto {
  @NotNull(message = "Value config không được để trống")
  @NotBlank(message = "Value config không được để trống")
  private String valueConfig;
}
