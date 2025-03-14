package com.project4.restaurant.domain;

import com.project4.restaurant.app.dto.admin.ConfigCreateDto;
import com.project4.restaurant.app.response.user.UserAccountResponse;
import com.project4.restaurant.app.response.admin.ConfigResponse;
import com.project4.restaurant.domain.entity.common.Config;
import com.project4.restaurant.domain.entity.user.UserAccount;
import com.project4.restaurant.domain.pojo.LeadershipDropListResponse;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface ModelMapper {
  UserAccountResponse toUserAccountResponse(UserAccount userAccount);

  List<LeadershipDropListResponse> toLeadershipDropListResponse(List<UserAccount> userAccounts);

  List<UserAccountResponse> toUserAccountResponse(List<UserAccount> userAccounts);

  Config toConfig(ConfigCreateDto dto);

  ConfigResponse toConfigResponse(Config config);

  default Page<ConfigResponse> toPageConfigResponse(Page<Config> config) {
    return config.map(this::toConfigResponse);
  }
}
