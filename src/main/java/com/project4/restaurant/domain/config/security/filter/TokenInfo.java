package com.project4.restaurant.domain.config.security.filter;

import com.project4.restaurant.domain.entity.type.AccountRole;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class TokenInfo extends UsernamePasswordAuthenticationToken {
  private Integer userId;
  private String username;
  private String fullName;
  private AccountRole role;

  public TokenInfo(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
    super(principal, credentials, authorities);
  }
}
