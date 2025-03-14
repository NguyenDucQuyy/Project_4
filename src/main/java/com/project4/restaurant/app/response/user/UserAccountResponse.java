package com.project4.restaurant.app.response.user;

import com.project4.restaurant.domain.entity.type.AccountRole;
import com.project4.restaurant.domain.entity.type.AccountState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountResponse {
  private Long id;
  private String username;
  private String fullName;
  private String phoneNumber;
  private String email;
  private AccountState state;
  private AccountRole role;
  private Long createdAt;
  private Long updatedAt;
}
