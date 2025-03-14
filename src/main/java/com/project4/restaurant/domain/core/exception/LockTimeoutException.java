package com.project4.restaurant.domain.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class LockTimeoutException extends Exception {
  public LockTimeoutException(String message) {
    super(message);
  }
}
