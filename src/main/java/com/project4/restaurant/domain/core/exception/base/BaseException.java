package com.project4.restaurant.domain.core.exception.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseException extends RuntimeException {
  protected int code;

  public BaseException(int code, String message) {
    super(message);
    setCode(code);
  }
}