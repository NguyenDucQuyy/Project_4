package com.project4.restaurant.app.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConfigCreateDto extends ConfigUpdateDto {
  @NotNull(message = "Key config không được để trống")
  @NotBlank(message = "Key config không được để trống")
  private String keyConfig;
}