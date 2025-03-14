package com.project4.restaurant.app.controller.publics;

import com.project4.restaurant.app.dto.authentication.*;
import com.project4.restaurant.app.dto.user.UpdateProfileDto;
import com.project4.restaurant.app.response.base.SingeResponse;
import com.project4.restaurant.app.response.user.UserAccountResponse;
import com.project4.restaurant.app.response.authen.ForgotPasswordTokenResponse;
import com.project4.restaurant.app.response.authen.RefreshTokenResponse;
import com.project4.restaurant.app.response.authen.TokenResponse;
import com.project4.restaurant.domain.config.security.filter.TokenInfo;
import com.project4.restaurant.domain.core.exception.BadRequestException;
import com.project4.restaurant.domain.service.AuthService;
import com.project4.restaurant.domain.service.UserAccountService;
import com.project4.restaurant.domain.service.base.BaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Validator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Tag(name = "Authentication", description = "")
@RestController
@RequestMapping("project_4/restaurant/auth")
public class AuthController {
  private final AuthService authService;
  private final UserAccountService userAccountService;
  private final Validator validator;
  private final BaseService baseService;

  public AuthController(AuthService authService, UserAccountService userAccountService, Validator validator, BaseService baseService) {
    this.authService = authService;
    this.userAccountService = userAccountService;
    this.validator = validator;
    this.baseService = baseService;
  }

  @Operation(summary = "Đăng nhập", description = "")
  @PostMapping("/signin")
  public ResponseEntity<TokenResponse> userLogin(@RequestBody @Valid LoginDto loginDto)  throws AuthenticationException {
    //try {
      return ResponseEntity.ok(authService.login(loginDto));
    //} catch(AuthenticationException exc){
      //throw new BadRequestException("Sai tên đăng nhập hoặc mật khẩu");
    //}
  }

  @Operation(summary = "API lấy lại token mới khi token cũ hết hạn", description = "")
  @PostMapping("/refresh-token")
  public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody @Valid RefreshTokenDto refreshTokenDto) {
    return ResponseEntity.ok(authService.refreshToken(refreshTokenDto));
  }

  @Operation(summary = "Đăng xuất", description = "")
  @DeleteMapping("signout")
  public void logout(@RequestBody @Valid LogoutDto logoutDto) {
    authService.logout(logoutDto);
  }

  @Operation(summary = "Quên mật khẩu", description = "Hệ thống sẽ gửi mã otp về email đã nhập")
  @PostMapping("/forgot-password")
  public ResponseEntity<Boolean> forgotPassword(@RequestBody @Valid ForgotPasswordDto forgotPasswordDto){
    userAccountService.forgotPassword(forgotPasswordDto);
    return ResponseEntity.ok(true);
  }

  @Operation(summary = "Xác thực otp cho quên mật khẩu", description = "Nhập otp hệ thống gửi về mail => hệ thống trả về token để sử dụng cho phiên đổi mật khẩu")
  @PostMapping("/verify-otp")
  public ResponseEntity<ForgotPasswordTokenResponse> verifyOtpPass(@RequestBody @Valid VerifyOtpDto verifyOtpDto, @RequestParam(required = false) String token) {
    if(verifyOtpDto == null){
      throw new BadRequestException("Vui lòng nhập thông tin xác thực otp");
    }
    verifyOtpDto.formatAllField();
    Set<ConstraintViolation<VerifyOtpDto>> violations = validator.validate(verifyOtpDto);
    if (!violations.isEmpty()) {
      throw new BadRequestException(baseService.processMessageError(violations.stream().map(ConstraintViolation::getMessage).toList()));
    }
    return ResponseEntity.ok(userAccountService.forgotPasswordVerifyOtp(verifyOtpDto.getEmail(), verifyOtpDto.getOtpCode()));
  }

  @Operation(summary = "Đổi mật khẩu", description = "Lấy token của api xác thực mật khẩu để sử dụng cho api này")
  @PostMapping("/reset-password")
  public ResponseEntity<Boolean> resetPassword(@RequestBody @Valid ResetPasswordDto resetPasswordDto, @RequestParam(required = false) String token) {
    if (resetPasswordDto == null) {
      throw new BadRequestException("Vui lòng nhập thông tin đổi mật khẩu");
    }
    resetPasswordDto.formatAllField();
    Set<ConstraintViolation<ResetPasswordDto>> violations = validator.validate(resetPasswordDto);
    if (!violations.isEmpty()) {
      throw new BadRequestException(baseService.processMessageError(violations.stream().map(ConstraintViolation::getMessage).toList()));
    }
    userAccountService.resetPassword(resetPasswordDto);
    return ResponseEntity.ok(true);
  }

  @PutMapping("/change-password")
  public ResponseEntity<Boolean> changePassword(
      Authentication authentication, @Valid @RequestBody ChangePasswordDto dto) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    return ResponseEntity.ok(userAccountService.changePassword(userDetails, dto));
  }

  @GetMapping("/profile")
  public ResponseEntity<UserAccountResponse> getUserAccountDetail() {
    TokenInfo tokenInfo = (TokenInfo) SecurityContextHolder.getContext().getAuthentication();
    return ResponseEntity.ok(userAccountService.getUserAccountProfile(tokenInfo.getUserId()));
  }

  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
  @PutMapping("/update-profile")
  public ResponseEntity<UserAccountResponse> updateProfile(@RequestBody @Valid UpdateProfileDto updateProfileDto){
    TokenInfo tokenInfo = (TokenInfo) SecurityContextHolder.getContext().getAuthentication();
    return ResponseEntity.ok(userAccountService.updateProfile(tokenInfo.getUserId(), updateProfileDto));
  }
}
