package com.project4.restaurant.domain.core.exception;

import com.project4.restaurant.domain.core.exception.base.BaseException;
import com.project4.restaurant.domain.core.exception.base.ErrorCode;

public class ResourceFoundException extends BaseException {

  public ResourceFoundException(String message) {
    super(ErrorCode.RESOURCE_FOUND, message);
  }
}
