package com.project4.restaurant.domain.pojo;

import com.project4.restaurant.domain.entity.type.AccountRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayloadInfo {
  private String username;
  private Integer userId;
  private Integer teamId;
  private List<AccountRole> roles;
}
