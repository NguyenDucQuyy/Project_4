package com.project4.restaurant.domain.core.exception;

import com.project4.restaurant.domain.core.exception.base.BaseException;
import com.project4.restaurant.domain.core.exception.base.ErrorCode;

public class UnauthorizedAccessException extends BaseException {
  public UnauthorizedAccessException(String message) {
    super(ErrorCode.UNAUTHORIZED_ACCESS, message);
  }
}
