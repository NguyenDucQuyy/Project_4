package com.project4.restaurant.app.response.authen;

import com.project4.restaurant.domain.entity.type.AccountRole;
import lombok.Data;

@Data
public class TokenResponse {
  private String accessToken;
  private String type = "Bearer";
  private String refreshToken;
  private String username;
  private AccountRole role;

  public TokenResponse(String accessToken, String refreshToken, String username, AccountRole role) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.username = username;
    this.role = role;
  }
}
