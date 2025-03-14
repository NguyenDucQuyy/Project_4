package com.project4.restaurant.domain.core.exception;

import com.project4.restaurant.domain.core.exception.base.BaseException;
import com.project4.restaurant.domain.core.exception.base.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Bạn không có quyền truy cập")
public class AccessDeniedException extends BaseException {
  public AccessDeniedException(String exception) {
    super(ErrorCode.ACCESS_DENIED, exception);
  }
}
