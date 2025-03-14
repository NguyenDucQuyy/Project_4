package com.project4.restaurant.domain.core.exception.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Data
@Log4j2
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {
  private int code;
  private String message;

  public static ExceptionResponse createFrom(BaseException e) {
    ExceptionResponse response = new ExceptionResponse();
    response.setMessage(e.getMessage());
    response.setCode(e.getCode());
    return response;
  }
  public static ExceptionResponse createFrom(Exception e) {
    ExceptionResponse response = new ExceptionResponse();
    response.setMessage(e.getMessage());
    response.setCode(500);
    return response;
  }
}