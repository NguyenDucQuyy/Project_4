package com.project4.restaurant.domain.entity.user;

import org.springframework.data.annotation.Transient;
import com.project4.restaurant.domain.entity.BaseEntity;
import com.project4.restaurant.domain.entity.type.AccountRole;
import com.project4.restaurant.domain.entity.type.AccountState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_account")
public class UserAccount extends BaseEntity implements UserDetails {
  @Transient
  public static final String SEQUENCE_NAME = "user_account_sequence";

  @Id
  private Long id;

  @Indexed(name = "user_name_unique_index", unique = true, direction = IndexDirection.DESCENDING)
  @Field(name = "username")
  private String username;

  @Field(name = "password")
  private String password;

  @Field(name = "full_name")
  private String fullName;

  @Field(name = "email")
  private String email;

  @Field(name = "phone_number")
  private String phoneNumber;

  @Field(name = "user_state")
  private AccountState state;

  @Field(name = "role")
  private AccountRole role;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + getRole().name()));
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return state == AccountState.ACTIVE;
  }
}
