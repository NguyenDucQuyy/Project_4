package com.project4.restaurant.domain.storage;

import com.project4.restaurant.domain.core.exception.ResourceFoundException;
import com.project4.restaurant.domain.entity.type.AccountState;
import com.project4.restaurant.domain.entity.user.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserStorage extends BaseStorage {
  public Page<UserAccount> findAll(Query condition, Pageable pageable) {
    return userAccountRepository.findAll(condition, pageable);
  }
  public List<UserAccount> findAll() {
    return userAccountRepository.findAll();
  }
  public List<UserAccount> findAllUserByState(AccountState state) {
    return userAccountRepository.findAllUserByState(state);
  }
  public UserAccount findById(Long userId) {
    return userAccountRepository.findById(userId)
        .orElseThrow(
          () -> new ResourceFoundException("Tài khoản người dùng không tồn tại")
        );
  }
  public UserAccount findByUsername(String username) {
    return userAccountRepository.findByUsername(username)
        .orElse(null);
  }
  public UserAccount findByEmail(String email) {
    return userAccountRepository.findByEmail(email);
  }
  public boolean existByEmail(String email) {
    return userAccountRepository.existsByEmail(email);
  }
  public boolean existsByPhoneNumber(String phoneNumber) {
    return userAccountRepository.existsByPhoneNumber(phoneNumber);
  }
  public void save(UserAccount userAccount) {
    userAccountRepository.save(userAccount);
  }
  public void delete(UserAccount userAccount) {
    userAccountRepository.delete(userAccount);
  }
}
