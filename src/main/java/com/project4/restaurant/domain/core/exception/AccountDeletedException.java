package com.project4.restaurant.domain.core.exception;

import com.project4.restaurant.domain.core.exception.base.BaseException;
import com.project4.restaurant.domain.core.exception.base.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.GONE)
public class AccountDeletedException extends BaseException {
  @Serial
  private static final long serialVersionUID = 1L;

  public AccountDeletedException(String exception) {
    super(ErrorCode.USER_DELETED, exception);
  }
}
