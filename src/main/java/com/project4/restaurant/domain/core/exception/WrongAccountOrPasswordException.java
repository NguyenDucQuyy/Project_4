package com.project4.restaurant.domain.core.exception;

import com.project4.restaurant.domain.core.exception.base.BaseException;
import com.project4.restaurant.domain.core.exception.base.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class WrongAccountOrPasswordException extends BaseException {
  public WrongAccountOrPasswordException() {
    super(ErrorCode.WRONG_ACCOUNT_OR_PASSWORD, "Sai tên đăng nhập hoặc mật khẩu");
  }
}