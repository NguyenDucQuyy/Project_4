package com.project4.restaurant.app.controller.publics;

import com.project4.restaurant.app.dto.authentication.*;
import com.project4.restaurant.app.response.base.SingeResponse;
import com.project4.restaurant.domain.core.exception.BadRequestException;
import com.project4.restaurant.domain.service.AuthService;
import com.project4.restaurant.domain.service.base.BaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Tag(name = "Register", description = "")
@RestController
@RequestMapping("project_4/restaurant/regis")
public class RegisController {
  private final AuthService authService;
  private final Validator validator;
  private final BaseService baseService;

  public RegisController(AuthService authService, Validator validator, BaseService baseService) {
    this.authService = authService;
    this.validator = validator;
    this.baseService = baseService;
  }

  @Operation(summary = "Đăng ký tài khoản agency", description = "hệ thống gửi otp về mail")
  @PostMapping("")
  public ResponseEntity<SingeResponse<Boolean>> register(@RequestBody @Valid RegisterDto registerDto) {
    authService.register(registerDto);
    return ResponseEntity.ok(SingeResponse.createFrom(true));
  }

  @Operation(summary = "Xác thực otp đăng ký tài khoản", description = "Nhập otp gửi về mail khi đăng ký tài khoản")
  @PostMapping("/verify-otp")
  public ResponseEntity<SingeResponse<Boolean>> verifyOtpRegis(@RequestBody @Valid VerifyOtpDto verifyOtpDto) {
    if (verifyOtpDto == null) {
      throw new BadRequestException("Vui lòng nhập thông tin xác thực otp");
    }
    verifyOtpDto.formatAllField();
    Set<ConstraintViolation<VerifyOtpDto>> violations = validator.validate(verifyOtpDto);
    if (!violations.isEmpty()) {
      throw new BadRequestException(baseService.processMessageError(violations.stream().map(ConstraintViolation::getMessage).toList()));
    }
    return ResponseEntity.ok(SingeResponse.createFrom(authService.registerVerifyOtp(verifyOtpDto)));
  }
}
