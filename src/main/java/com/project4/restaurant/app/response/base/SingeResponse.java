package com.project4.restaurant.app.response.base;

import lombok.Data;

@Data
public class SingeResponse<T> {
  private final T data;

  public static <T> SingeResponse<T> createFrom(T data) {
    return (SingeResponse<T>) new SingeResponse(data);
  }
}
