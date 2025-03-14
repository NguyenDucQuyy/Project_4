package com.project4.restaurant.domain.core.exception;

import com.project4.restaurant.domain.core.exception.base.BaseException;
import com.project4.restaurant.domain.core.exception.base.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NotAuthorizedException extends BaseException {
  private static final long serialVersionUID = 1L;

  public NotAuthorizedException() {
    super(ErrorCode.UNAUTHORIZED, "Không đủ quyền truy cập");
  }

  public NotAuthorizedException(String message) {
    super(ErrorCode.UNAUTHORIZED, message);
  }
}
