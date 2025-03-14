package com.project4.restaurant.domain.core.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

@Constraint(validatedBy = NoWhiteSpace.NoWhiteSpaceValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoWhiteSpace {
  String message() default "Field must not contain whitespace";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class NoWhiteSpaceValidator implements ConstraintValidator<NoWhiteSpace, String> {
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("^\\s|\\s$|\\s{2,}");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
      if (value == null) {
        return true;
      }
      return !WHITESPACE_PATTERN.matcher(value).find();
    }
  }
}