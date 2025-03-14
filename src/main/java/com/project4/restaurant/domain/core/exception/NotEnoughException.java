package com.project4.restaurant.domain.core.exception;

import com.project4.restaurant.domain.core.exception.base.BaseException;
import com.project4.restaurant.domain.core.exception.base.ErrorCode;

public class NotEnoughException extends BaseException {
    public NotEnoughException(String exception) {
        super(ErrorCode.NOT_ENOUGH,exception);
    }
}
