package com.project4.restaurant.app.controller.user;

import com.project4.restaurant.app.dto.user.UserAccountCreateDto;
import com.project4.restaurant.app.dto.user.UserAccountUpdateDto;
import com.project4.restaurant.app.dto.user.UserUpdateStateDto;
import com.project4.restaurant.app.response.user.UserAccountResponse;
import com.project4.restaurant.app.response.base.PageResponse;
import com.project4.restaurant.domain.entity.type.AccountState;
import com.project4.restaurant.domain.service.UserAccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "UserAccount", description = "")
@RestController
@RequestMapping("project_4/restaurant/account")
public class UserController {
  @Autowired private UserAccountService userAccountService;

  @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
  @GetMapping
  public ResponseEntity<?> accountList(
      @RequestParam(required = false) String search,
      @RequestParam(required = false) AccountState state,
      @PageableDefault(page = 0, size = 20) Pageable pageable) {
    return ResponseEntity.ok(PageResponse.createFrom(
        userAccountService.getAll(search, state, pageable)));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/{userId}")
  public ResponseEntity<UserAccountResponse> getUserAccountDetail(@PathVariable("userId") Long userId) {
    return ResponseEntity.ok(userAccountService.getUserAccountDetail(userId));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<UserAccountResponse> createAccount(@RequestBody @Valid UserAccountCreateDto userAccountCreateDto) {
    return ResponseEntity.ok(userAccountService.createAccount(userAccountCreateDto));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/{userId}")
  public ResponseEntity<UserAccountResponse> updateAccount(@PathVariable("userId") Long userId, @RequestBody @Valid UserAccountUpdateDto userAccountUpdateDto) {
    return ResponseEntity.ok(userAccountService.updateAccount(userId, userAccountUpdateDto));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{userId}")
  public ResponseEntity<Boolean> deleteAccount(@PathVariable("userId") Long userId) {
    return ResponseEntity.ok(userAccountService.deleteAccount(userId));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/{userId}/user-state")
  public ResponseEntity<Boolean> updateUserState(@PathVariable("userId") Long userId, @RequestBody UserUpdateStateDto userState) {
    return ResponseEntity.ok(userAccountService.updateUserState(userId, userState));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/users")
  public ResponseEntity<?> getAllUsersDropList() {
    return ResponseEntity.ok(userAccountService.getAllUsers());
  }
}
