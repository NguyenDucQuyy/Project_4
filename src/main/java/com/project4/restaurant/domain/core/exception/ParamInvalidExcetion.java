package com.project4.restaurant.domain.core.exception;

import com.project4.restaurant.domain.core.exception.base.BaseException;
import com.project4.restaurant.domain.core.exception.base.ErrorCode;

public class ParamInvalidExcetion extends BaseException {
    public ParamInvalidExcetion() {
        super(ErrorCode.NOT_VALID, "Not valid");
    }

    public ParamInvalidExcetion(String message) {
         super(ErrorCode.NOT_VALID, message);
    }
}

