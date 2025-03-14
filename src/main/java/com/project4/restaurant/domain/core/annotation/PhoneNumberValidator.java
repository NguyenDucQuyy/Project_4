package com.project4.restaurant.domain.core.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

  private static final String PHONE_NUMBER_PATTERN = "^(\\+84|0)[2|3|5|7|8|9][0-9]{7,9}$";
//  private static final String PHONE_NUMBER_PATTERN = "^(0|1|84|86)\\d{6,12}$";
  //private static final String PHONE_NUMBER_PATTERN = "^\\d{7,12}$";
  @Override
  public void initialize(ValidPhoneNumber constraintAnnotation) {
  }

  @Override
  public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
    if (phoneNumber == null) return true;
    if (phoneNumber.isEmpty()) return true;
    if (phoneNumber.isBlank()) return true;
    return phoneNumber.trim().matches(PHONE_NUMBER_PATTERN);
  }
}
