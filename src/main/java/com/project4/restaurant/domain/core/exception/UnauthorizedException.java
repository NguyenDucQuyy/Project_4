package com.project4.restaurant.domain.core.exception;

import com.project4.restaurant.domain.core.exception.base.BaseException;
import com.project4.restaurant.domain.core.exception.base.ErrorCode;

public class UnauthorizedException extends BaseException {
  public UnauthorizedException() {
    super(ErrorCode.UNAUTHORIZED, "Thông tin đăng nhập không hợp lệ");
  }
}
