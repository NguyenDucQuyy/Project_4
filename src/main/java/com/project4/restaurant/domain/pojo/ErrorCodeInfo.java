package com.project4.restaurant.domain.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorCodeInfo {
  private int errorCode;
  private String message;
}
