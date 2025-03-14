package com.project4.restaurant.domain.core.exception.base;

import lombok.experimental.UtilityClass;

@UtilityClass
public class VTPErrorMessage {
  public static final int SUCCESS = 200;
  public static final int BAD_REQUEST = 400;
  public static final int UNAUTHORIZED = 401;
  public static final int FORBIDDEN = 403;
}
