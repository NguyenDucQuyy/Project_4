package com.project4.restaurant.app.dto.user;

import com.project4.restaurant.domain.entity.type.AccountState;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserUpdateStateDto {
  @NotNull(message = "Vui lòng chọn")
  AccountState userState;
}
