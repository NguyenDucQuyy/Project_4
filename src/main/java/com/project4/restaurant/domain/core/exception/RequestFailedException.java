package com.project4.restaurant.domain.core.exception;

import com.project4.restaurant.domain.core.exception.base.BaseException;
import com.project4.restaurant.domain.core.exception.base.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequestFailedException extends BaseException {

  public RequestFailedException(String message) {
    super(ErrorCode.REQUEST_ERROR, message);
  }

  public RequestFailedException() {
    super(ErrorCode.REQUEST_ERROR, "Yêu cầu thất bại");
  }
}
