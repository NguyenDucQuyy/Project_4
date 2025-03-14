package com.project4.restaurant.domain.core.exception;

import com.project4.restaurant.domain.core.exception.base.BaseException;
import com.project4.restaurant.domain.core.exception.base.ErrorCode;

import java.io.Serial;

//@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnAuthenticationException extends BaseException {
  @Serial
  private static final long serialVersionUID = 1L;

  public UnAuthenticationException(String message) {
    super(ErrorCode.UNAUTHENTICATED, message);
  }
}
