package com.project4.restaurant.domain.repository;

import com.project4.restaurant.domain.entity.type.AccountState;
import com.project4.restaurant.domain.entity.user.UserAccount;
import com.project4.restaurant.domain.repository.base.MongoResourceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAccountRepository extends MongoResourceRepository<UserAccount, Long> {
  Page<UserAccount> findAllByIdNotNull(Pageable pageable);

  List<UserAccount> findAllUserByState(AccountState state);

  Optional<UserAccount> findByUsername(String username);

  UserAccount findByEmail(String email);

  boolean existsByEmail(String email);

  boolean existsByPhoneNumber(String phoneNumber);
}
