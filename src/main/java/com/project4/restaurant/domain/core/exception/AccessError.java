package com.project4.restaurant.domain.core.exception;

import com.project4.restaurant.domain.core.exception.base.BaseException;
import com.project4.restaurant.domain.core.exception.base.ErrorCode;

public class AccessError extends BaseException {
  private static final long serialVersionUID = 1L;

  public AccessError(String exception) {
    super(ErrorCode.ACCESS_ERROR, exception);
  }
}
