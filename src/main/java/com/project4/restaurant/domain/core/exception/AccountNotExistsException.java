package com.project4.restaurant.domain.core.exception;

import com.project4.restaurant.domain.core.exception.base.BaseException;
import com.project4.restaurant.domain.core.exception.base.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AccountNotExistsException extends BaseException {
  public AccountNotExistsException() {
    super(ErrorCode.ACCOUNT_NOT_EXISTS, "Không tìm thấy tài khoản người dùng");
  }

  public AccountNotExistsException(String message) {
    super(ErrorCode.ACCOUNT_NOT_EXISTS, message);
  }
}
